package com.ken.ebook.activity;

import java.util.ArrayList;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ken.ebook.R;
import com.ken.ebook.DAO.Database;
import com.ken.ebook.adapter.NavDrawerListAdapter;
import com.ken.ebook.fragment.FragmentAbout;
import com.ken.ebook.fragment.FragmentBooks;
import com.ken.ebook.fragment.FragmentBooks_GridView;
import com.ken.ebook.fragment.FragmentBooks_ListView;
import com.ken.ebook.fragment.FragmentFavorites;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.NavDrawerItem;
import com.ken.ebook.process.EpubBookHandler;
import com.ken.ebook.process.FileHandler;

public class ActivityMain extends FragmentActivity implements
		SearchView.OnQueryTextListener {
	Database db;
	SharedPreferences prefs = null;
	public static int FRAGMENT_STATE = 0x0;
	public static final int FRAGMENT_STATE_BOOKS = 0x1;
	public static final int FRAGMENT_STATE_FAVORITES = 0x2;
	public static final int FRAGMENT_STATE_BOOKSTORE = 0x3;
	public static final int FRAGMENT_STATE_CONTACT = 0x4;
	public static final int FRAGMENT_STATE_SETTING = 0x5;
	public static final int FRAGMENT_STATE_ABOUT = 0x6;

	public static String textSearch = "";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	public static ActionBarDrawerToggle mDrawerToggle;
	private SearchView mSearchView;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// actionbar color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0A80FF")));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initControls();

		// sliding menu

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// What's hot, We will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		prefs = getSharedPreferences("com.ken.ebook.EpubReader", MODE_PRIVATE);

		// create database
		db = new Database(this);

	}// end-func onCreate

	private void initControls() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		mSearchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();

		setupSearchView();
		return true;
	}// end-func onCreateOptionMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_add_book:
			Intent intent = new Intent(this, ActivityFileDialog.class);
			startActivityForResult(intent, FragmentBooks.REQUEST_EPUB_PATH);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}// end-fund onOptionsItemSelected

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case FragmentBooks.RESULT_CODE_OK:// xử lý khi xác nhận đã click vào
											// file
			FragmentBooks.pathFileEpub = data.getStringExtra("path");

			EpubBook newBook = EpubBookHandler
					.addNewEpubBook(FragmentBooks.pathFileEpub);

			Fragment fmBooks_content = getSupportFragmentManager()
					.findFragmentById(R.id.fm_content);
			if (fmBooks_content instanceof FragmentBooks_ListView) {
				FragmentBooks_ListView.adapter.eventAddNewBook(newBook);
			} else if (fmBooks_content instanceof FragmentBooks_GridView) {
				FragmentBooks_GridView.adapter.eventAddNewBook(newBook);
			} else {
				Toast.makeText(getApplicationContext(),
						"Đã thêm: " + newBook.getEpubBookName(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case FragmentBooks.RESULT_CODE_CANCEL:
			FragmentBooks.pathFileEpub = "";
			break;
		default:// nothing
			break;
		}

	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		// menu.findItem(R.id.action_add_book).setVisible(!drawerOpen);
		// menu.findItem(R.id.action_search).setVisible(!drawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}// end-func onPrepareOptionsMenu

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			FRAGMENT_STATE = FRAGMENT_STATE_BOOKS;
			setNoneFilter();
			fragment = new FragmentBooks(this);
			break;
		case 1:
			FRAGMENT_STATE = FRAGMENT_STATE_FAVORITES;
			setNoneFilter();
			fragment = new FragmentFavorites(this);
			break;
		case 2:
			FRAGMENT_STATE = FRAGMENT_STATE_ABOUT;
			setNoneFilter();
			fragment = new FragmentAbout();
			break;

		default:
			break;
		}

		if (fragment != null) {

			FragmentManager fragmentManager = getSupportFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}// end-func displayView

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}// end-func setTitle

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}// end-func onPostCreate

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}// end-func onConfigurationChangedseas

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}// end-class SlideMenuClickListener

	/*
	 * actionbar search
	 */
	private void setupSearchView() {

		mSearchView.setIconifiedByDefault(true);
		mSearchView.refreshDrawableState();

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			// List<SearchableInfo> searchables = searchManager
			// .getSearchablesInGlobalSearch();

		}

		mSearchView.setOnQueryTextListener(this);
		// mSearchView.setOnCloseListener(this);
	}

	public boolean onQueryTextChange(String newText) {
		textSearch = newText; // gán vào biến để fil ở fragment con

		Fragment fmBooks_content = getSupportFragmentManager()
				.findFragmentById(R.id.fm_content);

		switch (FRAGMENT_STATE) {
		case FRAGMENT_STATE_BOOKS:
			if (fmBooks_content instanceof FragmentBooks_ListView) {
				FragmentBooks_ListView.adapter.filter(newText.toString());
			}// end-if
			if (fmBooks_content instanceof FragmentBooks_GridView) {
				FragmentBooks_GridView.adapter.filter(newText.toString());
			}// end-if
			break;

		case FRAGMENT_STATE_FAVORITES:
			FragmentFavorites.adapter.filter(newText.toString());
			break;

		case FRAGMENT_STATE_BOOKSTORE:
			break;
		default:
			break;
		}

		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		if (!query.equals("")) {

		}
		return false;
	}

	// doan code chuyen man hinh
	public void navigationToView(Fragment _fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.enter, R.anim.exit,
		// R.anim.pop_enter, R.anim.pop_exit);
		// transaction.replace(R.id.fm_content, _fragment);
		transaction.add(R.id.fm_content, _fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// back pressed
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	// clear text search (ảo thôi)
	public void setNoneFilter() {
		textSearch = "";
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (prefs.getBoolean("firstrun", true)) {
			// create root folder
			FileHandler.createRootFolder();
			prefs.edit().putBoolean("firstrun", false).commit();
		}

	}
}
