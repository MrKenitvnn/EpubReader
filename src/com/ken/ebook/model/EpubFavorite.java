package com.ken.ebook.model;

public class EpubFavorite {
	private int favorite_id;
	private int epubBook_id;

	// constructor

	public int getFavorite_id() {
		return favorite_id;
	}

	public EpubFavorite setFavorite_id(int favorite_id) {
		this.favorite_id = favorite_id;
		return this;
	}

	public int getEpubBook_id() {
		return epubBook_id;
	}

	public EpubFavorite setEpubBook_id(int epubBook_id) {
		this.epubBook_id = epubBook_id;
		return this;
	}

	// all methods
}
