package com.ken.ebook.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ken.ebook.model.EpubBookmark;

public class EpubBookmarkDAO {

	Database dbhelper;
	SQLiteDatabase db;

	public EpubBookmarkDAO(Context _context) {
		dbhelper = new Database(_context);
	}

	public EpubBookmark getEpubBookmarkById(int id) {
		db = dbhelper.getReadableDatabase();

		String sql = "SELECT * FROM " + Database.TABLE_EPUB_BOOKMARK
					+ " WHERE " + Database.epubBook_id + " = " + id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		EpubBookmark epubBookmark = null;

		try {
			while (!c.isAfterLast()) {
				epubBookmark = new EpubBookmark();

				epubBookmark.setEpubBook_id(c.getInt(c
						.getColumnIndex(Database.epubBook_id)));
				epubBookmark.setComponentId(c.getString(c
						.getColumnIndex(Database.bookmarkComponentId)));
				epubBookmark.setPercent(c.getString(c
						.getColumnIndex(Database.bookmarkPercent)));
				c.moveToNext();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			epubBookmark = null;
		} finally {

			c.close();
			db.close();
		}

		return epubBookmark;
	}// end-func getEpubBookmarkById

	public void addEpubBookmark(EpubBookmark _epubBookmark) {
		try {
			db = dbhelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Database.epubBook_id, _epubBookmark.getEpubBook_id());
			values.put(Database.bookmarkComponentId, _epubBookmark.getComponentId());
			values.put(Database.bookmarkPercent, _epubBookmark.getPercent());

			db.insert(Database.TABLE_EPUB_BOOKMARK, null, values);
		} finally {
			db.close();
		}
	}// end-func addEpubBookmark

	public int editEpubBookmark(EpubBookmark _epubBookmark) {
		int result = -1;
		try {
			db = dbhelper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put(Database.bookmarkComponentId,_epubBookmark.getComponentId());
			values.put(Database.bookmarkPercent, _epubBookmark.getPercent());

			result = db.update(Database.TABLE_EPUB_BOOKMARK, 
								values,
								Database.epubBook_id + "=?",
								new String[] {String.valueOf(_epubBookmark.getEpubBook_id()) });
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.close();
		}
		return result;

	}// end-func editEpubBookmark

}
