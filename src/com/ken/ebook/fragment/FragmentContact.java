package com.ken.ebook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ken.ebook.R;

public class FragmentContact extends Fragment {

	public FragmentContact() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_community,
				container, false);

		// call the method setHasOptionsMenu, to have access to the menu from
		// your fragment
		setHasOptionsMenu(true);
		return rootView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    menu.findItem(R.id.action_search).setVisible(false);
	    menu.findItem(R.id.action_add_book).setVisible(false);
	    super.onCreateOptionsMenu(menu, inflater);
	    
	}

}
