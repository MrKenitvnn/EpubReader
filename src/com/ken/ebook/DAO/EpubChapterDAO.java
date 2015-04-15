package com.ken.ebook.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ken.ebook.model.EpubChapter;

public class EpubChapterDAO {
	Database dbhelper;
	SQLiteDatabase db;

	public EpubChapterDAO(Context _context) {
		dbhelper = new Database(_context);
	}

	public void addListChapter(List<EpubChapter> lst) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			for (EpubChapter item : lst) {
				values.put(Database.epubBook_id, item.getEpubBook_id());
				values.put(Database.chapterPath, item.getChapterPath());
				values.put(Database.chapterSrc, item.getChapterSrc());
				values.put(Database.chapterTitle, item.getChapterTitle());
				values.put(Database.chapterPlayOrder, item.getPlayOrder());

				db.insert(Database.TABLE_EPUB_CHAPTER, null, values);
			}
		} finally {
			db.close();
		}
	}// end-func addListChapter

	public List<EpubChapter> getListChapterByBookId(int bookId) {
		List<EpubChapter> lst = new ArrayList<EpubChapter>();
		db = dbhelper.getReadableDatabase();
		String sql = "SELECT * FROM " 
					+ Database.TABLE_EPUB_CHAPTER 
					+ " WHERE "
					+ Database.epubBook_id + "=" + bookId;

		Cursor c = db.rawQuery(sql, null);
		try {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					EpubChapter chapter = new EpubChapter();

					chapter.setChapterPath(c.getString(c
							.getColumnIndex(Database.chapterPath)));
					chapter.setChapterSrc(c.getString(c
							.getColumnIndex(Database.chapterSrc)));
					chapter.setChapterTitle(c.getString(c
							.getColumnIndex(Database.chapterTitle)));
					chapter.setPlayOrder(Integer.parseInt(c.getString(c
							.getColumnIndex(Database.chapterPlayOrder))));
					lst.add(chapter);
					c.moveToNext();
				}
			}
		} finally {
			c.close();
			db.close();
		}

		return lst;
	}// end-func getListChapterByBookId
}
