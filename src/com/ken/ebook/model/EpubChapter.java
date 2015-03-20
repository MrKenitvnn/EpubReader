package com.ken.ebook.model;

public class EpubChapter {
	private int chapterId;
	private int epubBook_id;
	private String chapterPath;
	private String chapterSrc;
	private String chapterTitle;
	private int playOrder;

	// getters & setters
	public String getChapterPath() {
		return chapterPath;
	}

	public int getEpubBook_id() {
		return epubBook_id;
	}

	public String getChapterSrc() {
		return chapterSrc;
	}

	public EpubChapter setChapterSrc(String chapterSrc) {
		this.chapterSrc = chapterSrc;
		return this;
	}

	public EpubChapter setEpubBook_id(int epubBook_id) {
		this.epubBook_id = epubBook_id;
		return this;
	}

	public EpubChapter setChapterPath(String chapterPath) {
		this.chapterPath = chapterPath;
		return this;
	}

	public int getChapterId() {
		return chapterId;
	}

	public EpubChapter setChapterId(int chapterId) {
		this.chapterId = chapterId;
		return this;
	}

	public String getChapterTitle() {
		return chapterTitle;
	}

	public EpubChapter setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
		return this;
	}

	public int getPlayOrder() {
		return playOrder;
	}

	public EpubChapter setPlayOrder(int playOrder) {
		this.playOrder = playOrder;
		return this;
	}

}
