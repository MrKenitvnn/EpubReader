package com.ken.ebook.model;

public class EpubBookmark {
	private int epubBook_id;
	private String componentId;
	private float percent;

	public EpubBookmark(int epubBook_id, String componentId, float percent) {
		super();
		this.epubBook_id = epubBook_id;
		this.componentId = componentId;
		this.percent = percent;
	}

	public int getEpubBook_id() {
		return epubBook_id;
	}

	public EpubBookmark setEpubBook_id(int epubBook_id) {
		this.epubBook_id = epubBook_id;
		return this;
	}

	public String getComponentId() {
		return componentId;
	}

	public EpubBookmark setComponentId(String componentId) {
		this.componentId = componentId;
		return this;
	}

	public float getPercent() {
		return percent;
	}

	public EpubBookmark setPercent(float percent) {
		this.percent = percent;
		return this;
	}

}
