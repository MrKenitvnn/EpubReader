package com.ken.ebook.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ken.ebook.R;
import com.ken.ebook.DAO.EpubFavoriteDAO;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.EpubFavorite;

public class FragmentFavorites_Adapter extends BaseAdapter {
	private static class ViewHolder {
		public ImageView imageView;
		public TextView tvEpubBookName;
		public TextView tvEpubBookAuthor;
		public CheckBox cbFavorite;
	}

	private Context context;
	private List<EpubBook> mLocations;
	private ArrayList<EpubBook> arraylist;
	private LayoutInflater mInflater;
	private EpubFavoriteDAO favoriteDao;
	private ArrayList<EpubFavorite> listFavorites;

	// constructor
	public FragmentFavorites_Adapter(Context context, List<EpubBook> locations) {
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLocations = locations;
		this.arraylist = new ArrayList<EpubBook>();
		this.arraylist.addAll(locations);
		this.favoriteDao = new EpubFavoriteDAO(context);
		this.listFavorites = new ArrayList<EpubFavorite>();
		this.listFavorites.addAll(favoriteDao.loadAllEpubFavorites());
		favoriteDao.close();
		// itemChecked = new boolean[locations.size()];
	}// end-func

	@Override
	public int getCount() {
		if (mLocations != null) {
			return mLocations.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position);
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position).getEpubBook_id();
		}

		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder viewHolder;

		if (view == null) {
			view = mInflater.inflate(R.layout.item_fragment_favorites, parent,
					false);

			viewHolder = new ViewHolder();

			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.iv_item_list_favorites);
			viewHolder.tvEpubBookName = (TextView) view
					.findViewById(R.id.tvEpubBookName_favorites);
			viewHolder.tvEpubBookAuthor = (TextView) view
					.findViewById(R.id.tvEpubBookAuthor_favorites);
			viewHolder.cbFavorite = (CheckBox) view
					.findViewById(R.id.cbFavorite_favorites);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// create an instance of EpubBook
		final EpubBook locationModel = mLocations.get(position);

		if (locationModel.getEpubCover() != null) { // when doesn't have cover
			File imgFile = new File(locationModel.getEpubCover());
			if (imgFile.exists()) {

				// viewHolder.imageView.setImageBitmap(myBitmap);
				Uri uri = Uri.fromFile(imgFile);
				viewHolder.imageView.setImageURI(uri);
			}
		} else {
			Drawable myDrawable = view.getResources().getDrawable(
					R.drawable.default_book_cover);
			viewHolder.imageView.setImageDrawable(myDrawable);
		}// end-if

		viewHolder.tvEpubBookName.setText(locationModel.getEpubBookName());
		viewHolder.tvEpubBookAuthor.setText(locationModel.getEpubBookAuthor());

		for (int i = 0; i < listFavorites.size(); i++) {
			if (locationModel.getEpubBook_id() == listFavorites.get(i)
					.getEpubBook_id()) {
				viewHolder.cbFavorite.setChecked(true);
			}
		}

		viewHolder.cbFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (viewHolder.cbFavorite.isChecked()) {
					// itemChecked[position] = true;
					favoriteDao.addEpubFavorite(locationModel.getEpubBook_id());
				} else {
					// itemChecked[position] = false;
					mLocations.remove(locationModel);
					notifyDataSetChanged();
					favoriteDao.delEpubFavorite(locationModel.getEpubBook_id());			
				}// end-if
			}// end-func
		});
		return view;
	}
	// event del a book
		public void eventDelABook(int id) {
			mLocations.remove(id);
			notifyDataSetChanged();
		}

	// Filter
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mLocations.clear();
		if (charText.length() == 0) {
			mLocations.addAll(arraylist);
		} else {
			for (EpubBook wp : arraylist) {
				if (wp.getEpubBookName().toLowerCase(Locale.getDefault())
						.contains(charText)
						|| wp.getEpubBookAuthor()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					mLocations.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

	public void closeSearch() {
		// mLocations.addAll(arraylist);
		notifyDataSetChanged();
	}

}
