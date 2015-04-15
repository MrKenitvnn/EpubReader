package com.ken.ebook.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ken.ebook.R;
import com.ken.ebook.DAO.EpubBookDAO;
import com.ken.ebook.DAO.EpubChapterDAO;
import com.ken.ebook.DAO.EpubCssDAO;
import com.ken.ebook.DAO.EpubFavoriteDAO;
import com.ken.ebook.activity.ActivityMain;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.EpubFavorite;

public class FragmentBooks extends Fragment implements OnClickListener {
	public static final int 
						ANIMATION_DURATION = 400,
						
						REQUEST_EPUB_PATH = 0x0,
						RESULT_CODE_OK = 0x1,
						RESULT_CODE_CANCEL = 0x2;

	public static String 			pathFileEpub = "";
	public static EpubBookDAO		bookDAO;
	public static EpubChapterDAO	chapterDAO;
	public static EpubFavoriteDAO	favoriteDao;
	public static EpubCssDAO		cssDAO;
	public static Context			context;
	public static List<EpubBook>	listEpubBook = null;
	public static List<EpubFavorite> listFavorites = null;
	ActivityMain main;

	Button btnToGrid, btnToList;

	public FragmentBooks(Context _context) {
		context = _context;
	}

////////////////////////////////////////////////////////////////////////////////
// fragment life cycle
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_books, container,
				false);
		// new LoadDataTask().execute();

		main = (ActivityMain) getActivity();
		main .navigationToView(new FragmentBooks_ListView());

		bookDAO		= new EpubBookDAO(context);
		chapterDAO	= new EpubChapterDAO(context);
		favoriteDao = new EpubFavoriteDAO(context);
		cssDAO		= new EpubCssDAO(context);

		listEpubBook	= new ArrayList<EpubBook>();
		listEpubBook	.addAll(bookDAO.loadAllEpubBook());
		listFavorites	= new ArrayList<EpubFavorite>();
		listFavorites	.addAll(favoriteDao.loadAllEpubFavorites());

		return rootView;
	}// end-func onCreateView

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		main = (ActivityMain) getActivity();
		// init controls
		btnToGrid = (Button) main.findViewById(R.id.btnToGrid);
		btnToList = (Button) main.findViewById(R.id.btnToList);

		btnToGrid.setOnClickListener(this);
		btnToList.setOnClickListener(this);

	}// end-func onActivityCreated

	@Override
	public void onResume() {
		super.onResume();

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					// handle back button
					getActivity().finish();
					return true;
				}
				return false;
			}
		});

	}// end-func onResume

	@Override
	public void onDestroy() {
		super.onDestroy();
	}// end-func onDestroy

////////////////////////////////////////////////////////////////////////////////
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnToGrid:
			// main.navigationToView(new FragmentBooks_GridView());
			// btnToGrid.setVisibility(View.GONE);
			// btnToList.setVisibility(View.VISIBLE);

			new ViewToGridTask().execute();
			break;
		case R.id.btnToList:
			// main.navigationToView(new FragmentBooks_ListView());
			// btnToList.setVisibility(View.GONE);
			// btnToGrid.setVisibility(View.VISIBLE);

			new ViewToListTask().execute();
			break;
		default:
			break;
		}
	}


////////////////////////////////////////////////////////////////////////////////
	
	boolean clickable = false;

	private class ViewToListTask extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			dialog.setMessage("Loading..");
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			clickable = false;
			main.navigationToView(new FragmentBooks_ListView());
			clickable = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (clickable) {
				btnToList.setClickable(true);
				btnToGrid.setClickable(true);
			} else {
				btnToList.setClickable(false);
				btnToGrid.setClickable(false);
			}

			btnToList.setVisibility(View.GONE);
			btnToGrid.setVisibility(View.VISIBLE);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}// end-asynctask ViewToListTask

////////////////////////////////////////////////////////////////////////////////
	
	private class ViewToGridTask extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			dialog.setMessage("Loading..");
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			clickable = false;
			// TODO Auto-generated method stub
			main.navigationToView(new FragmentBooks_GridView());
			clickable = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (clickable) {
				btnToList.setClickable(true);
				btnToGrid.setClickable(true);
			} else {
				btnToList.setClickable(false);
				btnToGrid.setClickable(false);
			}
			btnToGrid.setVisibility(View.GONE);
			btnToList.setVisibility(View.VISIBLE);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}// end-asynctask ViewToGridTask

}
