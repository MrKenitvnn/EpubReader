package com.ken.ebook.fragment;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ken.ebook.R;
import com.ken.ebook.DAO.EpubBookDAO;
import com.ken.ebook.activity.ActivityMain;
import com.ken.ebook.activity.ActivityReading;
import com.ken.ebook.adapter.FragmentBooks_ListAdapter;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.process.FileHandler;

public class FragmentBooks_ListView extends Fragment implements
		OnItemLongClickListener, OnItemClickListener {
	static final int ANIMATION_DURATION = 400;
	ListView lvEpubBook;
	public static FragmentBooks_ListAdapter adapter;
	public static ProgressDialog pd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_books_list,
				container, false);

		// init controls
		lvEpubBook = (ListView) rootView.findViewById(R.id.lvEpubBook);

		adapter = new FragmentBooks_ListAdapter(getActivity(),
				FragmentBooks.listEpubBook);
		// EbookEpubApplication.getEpubDataLocation());

		lvEpubBook.setAdapter(adapter);

		lvEpubBook.setTextFilterEnabled(true);
		lvEpubBook.setOnItemClickListener(this);
		lvEpubBook.setOnItemLongClickListener(this);

		adapter.notifyDataSetChanged();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter.filter(ActivityMain.textSearch);
	}// end-func onActivityCreated

	// event click
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		EpubBook book = FragmentBooks.listEpubBook.get(position);
		Intent intent = new Intent(getActivity(), ActivityReading.class);
		intent.putExtra("BOOK", book);
		startActivity(intent);

	};

	// end-func onItemClick

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FragmentBooks.context);

		builder.setTitle("Delete a book");
		builder.setIcon(R.drawable.ic_action_delete);

		builder.setMessage("Do you want to delete this book?");
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				view.animate().alpha(0).setDuration(ANIMATION_DURATION)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								EpubBook book = (EpubBook) adapter
										.getItem(position);
								if (FragmentBooks.bookDAO.delEpubBook(Integer
										.parseInt(book.getEpubFolder())) > 0) {
									FragmentBooks.favoriteDao
											.delEpubFavorite(Integer
													.parseInt(book
															.getEpubFolder()));
									FileHandler.deleteBookFolder(new File(
											FileHandler.rootPath
													+ book.getEpubFolder()));
									adapter.eventDelABook(position);
								} else {
									Log.d("delete epub book",
											"failed FragmentBooks_ListView line 117");
								}
							}
						});
			}
		});
		dialog = builder.create();
		dialog.show();

		return true;
	}// end-func onItemLongClick

	@Override
	public void onDestroy() {
		super.onDestroy();
		EpubBookDAO.close();
	}

	
	
	
	
	
	
	
	
	
	
}
