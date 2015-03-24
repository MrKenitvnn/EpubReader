package com.ken.ebook.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EpubBook implements Serializable{
	private int epubBook_id;
	private String epubBookName;
	private String epubBookAuthor;
	private String epubCover;
	private String epubFolder;
	private String epubFilePath;
	private String contentFilePath;
	private String ncxFilePath;

	public EpubBook() {
	};

	public EpubBook(String epubBookName, String epubBookAuthor,
			String epubCover, String epubFolder, String epubFilePath,
			String contentFilePath, String ncxFilePath) {
		super();
		this.epubBookName = epubBookName;
		this.epubBookAuthor = epubBookAuthor;
		this.epubCover = epubCover;
		this.epubFolder = epubFolder;
		this.epubFilePath = epubFilePath;
		this.contentFilePath = contentFilePath;
		this.ncxFilePath = ncxFilePath;
	}

	// getters & setters
	public int getEpubBook_id() {
		return epubBook_id;
	}

	public EpubBook setEpubBook_id(int epubBook_id) {
		this.epubBook_id = epubBook_id;
		return this;
	}

	public String getEpubBookName() {
		return epubBookName;
	}

	public EpubBook setEpubBookName(String epubBookName) {
		this.epubBookName = epubBookName;
		return this;
	}

	public String getEpubBookAuthor() {
		return epubBookAuthor;
	}

	public EpubBook setEpubBookAuthor(String epubBookAuthor) {
		this.epubBookAuthor = epubBookAuthor;
		return this;
	}

	public String getEpubCover() {
		return epubCover;
	}

	public EpubBook setEpubCover(String epubCover) {
		this.epubCover = epubCover;
		return this;
	}

	public String getContentFilePath() {
		return contentFilePath;
	}

	public EpubBook setContentFilePath(String contentFilePath) {
		this.contentFilePath = contentFilePath;
		return this;
	}

	public String getEpubFolder() {
		return epubFolder;
	}

	public EpubBook setEpubFolder(String _epubFolder) {
		this.epubFolder = _epubFolder;
		return this;
	}

	public String getEpubFilePath() {
		return epubFilePath;
	}

	public EpubBook setEpubFilePath(String epubFilePath) {
		this.epubFilePath = epubFilePath;
		return this;
	}

	public String getNcxFilePath() {
		return ncxFilePath;
	}

	public EpubBook setNcxFilePath(String ncxFilePath) {
		this.ncxFilePath = ncxFilePath;
		return this;
	}

	// all methods
}
