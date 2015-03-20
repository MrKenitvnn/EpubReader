package com.ken.ebook.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
	static final int ANIMATION_DURATION = 400;
	public static final int REQUEST_EPUB_PATH = 0x0;
	public static final int RESULT_CODE_OK = 0x1;
	public static final int RESULT_CODE_CANCEL = 0x2;

	public static String pathFileEpub = "";
	public static EpubBookDAO bookDAO;
	public static EpubChapterDAO chapterDAO;
	public static EpubFavoriteDAO favoriteDao;
	public static EpubCssDAO cssDAO;
	public static Context context;
	public static List<EpubBook> listEpubBook = null;
	public static ArrayList<EpubFavorite> listFavorites = null;

	Button btnToGrid, btnToList;

	public FragmentBooks(Context _context) {
		context = _context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_books, container,
				false);
		ActivityMain main = (ActivityMain) getActivity();
		main.navigationToView(new FragmentBooks_ListView());
		// STATE_FRAGMENT = STATE_FRAGMENT_LIST;

		bookDAO = new EpubBookDAO(context);
		chapterDAO = new EpubChapterDAO(context);
		favoriteDao = new EpubFavoriteDAO(context);
		cssDAO = new EpubCssDAO(context);

		listEpubBook = new ArrayList<EpubBook>();
		listEpubBook.addAll(bookDAO.loadAllEpubBook());
		listFavorites = new ArrayList<EpubFavorite>();
		listFavorites.addAll(favoriteDao.loadAllEpubFavorites());

		return rootView;
	}// end-func onCreateView

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActivityMain main = (ActivityMain) getActivity();

		// init controls
		btnToGrid = (Button) main.findViewById(R.id.btnToGrid);
		btnToList = (Button) main.findViewById(R.id.btnToList);

		btnToGrid.setOnClickListener(this);
		btnToList.setOnClickListener(this);
		// setupSearchView();

	}// end-func onActivityCreated

	@Override
	public void onClick(View v) {
		ActivityMain main = (ActivityMain) getActivity();
		switch (v.getId()) {
		case R.id.btnToGrid:
			// STATE_FRAGMENT = STATE_FRAGMENT_GRID;
			btnToGrid.setVisibility(View.GONE);
			btnToList.setVisibility(View.VISIBLE);
			main.navigationToView(new FragmentBooks_GridView());
			break;
		case R.id.btnToList:
			// STATE_FRAGMENT = STATE_FRAGMENT_LIST;
			btnToList.setVisibility(View.GONE);
			btnToGrid.setVisibility(View.VISIBLE);
			main.navigationToView(new FragmentBooks_ListView());
			break;
		default:
			break;
		}
	}

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
	}
}
