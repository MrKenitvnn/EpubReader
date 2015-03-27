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

import com.ken.ebook.R;
import com.ken.ebook.fragment.FragmentBooks;
import com.ken.ebook.model.EpubBook;

public class FragmentBooks_ListAdapter extends BaseAdapter {

	private static class ViewHolder {
		public boolean needInflate;
		public ImageView imageView;
		public TextView tvEpubBookName;
		public TextView tvEpubBookAuthor;
		public CheckBox cbFavorite;
	}

	private Context context;
	private static List<EpubBook> mLocations;
	private ArrayList<EpubBook> arraylist;
	private LayoutInflater mInflater;
	
	/**
	 * @return the mLocations
	 */
	public static List<EpubBook> getmLocations() {
		return mLocations;
	}

	/**
	 * @param mLocations the mLocations to set
	 */
	public static void setmLocations(List<EpubBook> mLocations) {
		FragmentBooks_ListAdapter.mLocations = mLocations;
	}

	// constructor
	public FragmentBooks_ListAdapter(Context context, List<EpubBook> locations) {
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLocations = locations;
		this.arraylist = new ArrayList<EpubBook>();
		this.arraylist.addAll(locations);
		// this.listFavorites = new ArrayList<EpubFavorite>();
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
			view = mInflater.inflate(R.layout.item_fragment_books_list, parent,
					false);

			viewHolder = new ViewHolder();

			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.iv_item_list);
			viewHolder.tvEpubBookName = (TextView) view
					.findViewById(R.id.tvEpubBookName);
			viewHolder.tvEpubBookAuthor = (TextView) view
					.findViewById(R.id.tvEpubBookAuthor);
			viewHolder.cbFavorite = (CheckBox) view
					.findViewById(R.id.cbFavorite);

			view.setTag(viewHolder);

		} else if (((ViewHolder) convertView.getTag()).needInflate) {
			view = mInflater.inflate(R.layout.item_fragment_books_list, parent,
					false);

			viewHolder = new ViewHolder();

			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.iv_item_list);
			viewHolder.tvEpubBookName = (TextView) view
					.findViewById(R.id.tvEpubBookName);
			viewHolder.tvEpubBookAuthor = (TextView) view
					.findViewById(R.id.tvEpubBookAuthor);
			viewHolder.cbFavorite = (CheckBox) view
					.findViewById(R.id.cbFavorite);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}// end-if
			// create an instance of EpubBook
		final EpubBook locationModel = mLocations.get(position);

		if (locationModel.getEpubCover() != null) { // when doesn't have cover
			File imgFile = new File(locationModel.getEpubCover());
			if (imgFile.exists()) {

				// viewHolder.imageView.setImageBitmap(myBitmap);
				Uri uri = Uri.fromFile(imgFile);
				viewHolder.imageView.setImageURI(uri);
			}// end-if
		} else {
			Drawable myDrawable = view.getResources().getDrawable(
					R.drawable.default_book_cover);
			viewHolder.imageView.setImageDrawable(myDrawable);
		}// end-if

		viewHolder.tvEpubBookName.setText(locationModel.getEpubBookName());
		viewHolder.tvEpubBookAuthor.setText(locationModel.getEpubBookAuthor());

		for (int i = 0; i < FragmentBooks.listFavorites.size(); i++) {
			if (locationModel.getEpubBook_id() == FragmentBooks.listFavorites
					.get(i).getEpubBook_id()) {
				viewHolder.cbFavorite.setChecked(true);
			}
		}// end-for

		viewHolder.cbFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (viewHolder.cbFavorite.isChecked()) {
					FragmentBooks.favoriteDao.addEpubFavorite(locationModel
							.getEpubBook_id());

				} else {
					FragmentBooks.favoriteDao.delEpubFavorite(locationModel
							.getEpubBook_id());

				}// end-if
			}// end-func
		});
		return view;
	}

	// event add a book
	public void eventAddNewBook(EpubBook wp) {
		if (wp != null) {
			// mLocations.clear();
			// for (EpubBook wpp : arraylist) {
			// mLocations.add(wpp);
			// }
			mLocations.add(wp);
			notifyDataSetChanged();
		}
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
