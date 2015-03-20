package com.ken.ebook.model;

public class EpubCss {
	private int css_id;
	private int epubBook_id;
	private String cssPath;

	public EpubCss() {

	}

	public EpubCss(int css_id, int epubBook_id, String cssPath) {
		super();
		this.css_id = css_id;
		this.epubBook_id = epubBook_id;
		this.cssPath = cssPath;
	}

	// getters & setters

	public int getCss_id() {
		return css_id;
	}

	public EpubCss setCss_id(int css_id) {
		this.css_id = css_id;
		return this;
	}

	public int getEpubBook_id() {
		return epubBook_id;
	}

	public EpubCss setEpubBook_id(int epubBook_id) {
		this.epubBook_id = epubBook_id;
		return this;
	}

	public String getCssPath() {
		return cssPath;
	}

	public EpubCss setCssPath(String cssPath) {
		this.cssPath = cssPath;
		return this;
	}

}
