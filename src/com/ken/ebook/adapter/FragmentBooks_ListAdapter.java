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
		private ImageView imageView;	// hiển thị cover
		private TextView 
				tvEpubBookName,			// tên sách
				tvEpubBookAuthor;		// tác giả
		private CheckBox cbFavorite;	// trạng thái yêu thích

		private boolean needInflate;
	}

	private static List<EpubBook>	mLocations; // danh sách sách hiển thị 
	private ArrayList<EpubBook>		arraylist;	// danh sách sách trung gian để lọc khi tìm kiếm
	private LayoutInflater			mInflater;
	
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
		mInflater		= (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLocations		= locations;
		this.arraylist	= new ArrayList<EpubBook>();
		this.arraylist	.addAll(locations);
	}// end-func

////////////////////////////////////////////////////////////////////////////////
	
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
			viewHolder	= new ViewHolder();
			view		= mInflater.inflate(R.layout.item_fragment_books_list, parent, false);

			viewHolder.imageView 		= (ImageView) view.findViewById(R.id.iv_item_list);
			viewHolder.tvEpubBookName 	= (TextView) view.findViewById(R.id.tvEpubBookName);
			viewHolder.tvEpubBookAuthor = (TextView) view.findViewById(R.id.tvEpubBookAuthor);
			viewHolder.cbFavorite		= (CheckBox) view.findViewById(R.id.cbFavorite);

			view.setTag(viewHolder);
		} else if ( ((ViewHolder) convertView.getTag()).needInflate ){
			viewHolder	= new ViewHolder();
			view		= mInflater.inflate(R.layout.item_fragment_books_list, parent, false);
			
			viewHolder.imageView		= (ImageView) view.findViewById(R.id.iv_item_list);
			viewHolder.tvEpubBookName	= (TextView) view.findViewById(R.id.tvEpubBookName);
			viewHolder.tvEpubBookAuthor = (TextView) view.findViewById(R.id.tvEpubBookAuthor);
			viewHolder.cbFavorite		= (CheckBox) view.findViewById(R.id.cbFavorite);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}// end-if
		
		// create an instance of EpubBook
		final EpubBook locationModel = mLocations.get(position);

		if (locationModel.getEpubCover() != null) { // when doesn't have cover
			File imgFile = new File(locationModel.getEpubCover());
			if (imgFile.exists()) {
				Uri uri		= Uri.fromFile(imgFile);
				viewHolder	.imageView.setImageURI(uri);
			}// end-if
		} else {
			Drawable myDrawable = view.getResources().getDrawable(R.drawable.default_book_cover);
			viewHolder.imageView.setImageDrawable(myDrawable);
		}// end-if

		viewHolder.tvEpubBookName	.setText(locationModel.getEpubBookName());
		viewHolder.tvEpubBookAuthor	.setText(locationModel.getEpubBookAuthor());

		// với mỗi cuốn sách, duyệt qua danh sách yêu thích để hiển thị checkbox
		for (int i = 0; i < FragmentBooks.listFavorites.size(); i++) {
			if (locationModel.getEpubBook_id() == FragmentBooks
													.listFavorites		// danh sách yêu thích
													.get(i)				// lấy ra phần tử
													.getEpubBook_id())	// lấy ra id sách 
			{
				viewHolder.cbFavorite.setChecked(true);
			}// end-if
		}// end-for

		viewHolder.cbFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (viewHolder.cbFavorite.isChecked()) {
					FragmentBooks.favoriteDao
									.addEpubFavorite(locationModel.getEpubBook_id());
				} else {
					FragmentBooks.favoriteDao
									.delEpubFavorite(locationModel.getEpubBook_id());
				}// end-if
			}// end-func
		});
		return view;
	}

////////////////////////////////////////////////////////////////////////////////

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

////////////////////////////////////////////////////////////////////////////////
	
	// lọc khi nhập ký tự trên searchview
	public void filter(String charText) {
		charText	= charText.toLowerCase(Locale.getDefault());
		mLocations	.clear();
		if (charText.length() == 0) {
			mLocations.addAll(arraylist);
		} else {
			for (EpubBook wp : arraylist) {
				if (wp.getEpubBookName().toLowerCase(Locale.getDefault()).contains(charText)
					|| wp.getEpubBookAuthor().toLowerCase(Locale.getDefault()).contains(charText)) {
					mLocations.add(wp);
				}// end-if
			}// end-for
		}
		notifyDataSetChanged();
	}// end-func filter

	public void closeSearch() {
		// mLocations.addAll(arraylist);
		notifyDataSetChanged();
	}

}
