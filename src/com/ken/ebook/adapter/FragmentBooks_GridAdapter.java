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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ken.ebook.R;
import com.ken.ebook.model.EpubBook;

public class FragmentBooks_GridAdapter extends BaseAdapter {

	private class ViewHolder {
		public ImageView imageView;
		public TextView tvEpubBookName;
	}

	private List<EpubBook> mLocations;
	private ArrayList<EpubBook> arraylist;
	private LayoutInflater mInflater;

	public FragmentBooks_GridAdapter(Context context, List<EpubBook> locations) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLocations = locations;
		this.arraylist = new ArrayList<EpubBook>();
		this.arraylist.addAll(locations);
	}

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

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = mInflater.inflate(R.layout.item_fragment_books_grid, parent,
					false);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.grid_image);
			viewHolder.tvEpubBookName = (TextView) view
					.findViewById(R.id.grid_label);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// create an instance of EpubBook
		EpubBook locationModel = mLocations.get(position);

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

		return view;
	}

	// event add a book
	public void eventAddNewBook(EpubBook wp) {
		if (wp != null) {
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
