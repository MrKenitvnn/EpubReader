package com.ken.ebook.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.ken.ebook.R;
import com.ken.ebook.DAO.EpubBookDAO;
import com.ken.ebook.DAO.EpubFavoriteDAO;
import com.ken.ebook.activity.ActivityReading;
import com.ken.ebook.adapter.FragmentFavorites_Adapter;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.process.FileHandler;

public class FragmentFavorites extends Fragment implements
		OnItemLongClickListener, OnItemClickListener {
	static final int ANIMATION_DURATION = 400;
	public static Context context;
	private ListView lvFavorites;
	CheckBox cbFavorite;

	public static FragmentFavorites_Adapter adapter;
	EpubFavoriteDAO favoriteDAO;
	EpubBookDAO bookDAO;
	List<EpubBook> listEpubBook = null;

	public FragmentFavorites(Context _context) {
		context = _context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_favorites,
				container, false);

		// init controls
		lvFavorites = (ListView) rootView.findViewById(R.id.lvFavorites);
		cbFavorite = (CheckBox) getActivity().findViewById(R.id.cbFavorite);

		favoriteDAO = new EpubFavoriteDAO(context);
		bookDAO = new EpubBookDAO(context);
		listEpubBook = new ArrayList<EpubBook>();
		// load data from sql
		listEpubBook = favoriteDAO.loadAllEpubBookFavorites();

		// adapter
		adapter = new FragmentFavorites_Adapter(context, listEpubBook);
		lvFavorites.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		// event
		lvFavorites.setTextFilterEnabled(true);
		lvFavorites.setOnItemClickListener(this);
		lvFavorites.setOnItemLongClickListener(this);

		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		final ProgressDialog progressDialog;
		progressDialog = ProgressDialog.show(getActivity(), "", "Loading..");
		new Thread() {
			public void run() {
				Intent intent = new Intent(context, ActivityReading.class);
				intent.putExtra("BOOK", listEpubBook.get(position));
				startActivity(intent);
				progressDialog.dismiss();
			}
		}.start();
	}// end-func onItemClick

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
			final int position, long id) {

		return true;
	}// end-func onItemLongClick

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.action_add_book).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);

	}

}
