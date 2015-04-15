package com.ken.ebook.DAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ken.ebook.model.EpubCss;

public class EpubCssDAO {

	Database dbhelper;
	SQLiteDatabase db;

	public EpubCssDAO(Context _context) {
		dbhelper = new Database(_context);
	}

	public void addListCss(int book_id, List<File> lst) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			for (File item : lst) {
				values.put(Database.epubBook_id, book_id);
				values.put(Database.cssPath, item.getPath());

				db.insert(Database.TABLE_EPUB_CSS, null, values);
			}
		} finally {
			db.close();
		}
	}// end-func addListCss

	public List<EpubCss> getListCssByBookId(int bookId) {
		List<EpubCss> lst = new ArrayList<EpubCss>();
		db = dbhelper.getReadableDatabase();
		String sql = "SELECT * FROM " + Database.TABLE_EPUB_CSS + " WHERE "
				+ Database.epubBook_id + "=" + bookId;

		Cursor c = db.rawQuery(sql, null);
		try {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					EpubCss chapter = new EpubCss();

					chapter.setCssPath(c.getString(c.getColumnIndex(Database.cssPath)));

					lst.add(chapter);
					c.moveToNext();
				}// end-while
			}// end-if
		} finally {
			c.close();
			db.close();
		}// end-try

		return lst;
	}// end-func getListCssByBookId

}
