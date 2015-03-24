package com.ken.ebook.process;

import java.util.Comparator;

import com.ken.ebook.model.EpubChapter;

public class ComparatorChapterByPlayOrder implements Comparator<EpubChapter> {
	@Override
	public int compare(EpubChapter a, EpubChapter b) {
		return a.getPlayOrder() < b.getPlayOrder() ? -1 : a.getPlayOrder() == b
				.getPlayOrder() ? 0 : 1;
	}

}
