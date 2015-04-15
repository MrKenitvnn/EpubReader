package com.ken.ebook.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.EpubFavorite;

public class EpubFavoriteDAO {

	Database dbhelper;
	SQLiteDatabase db;

	public EpubFavoriteDAO(Context _context) {
		dbhelper = new Database(_context);
	}

	// start-func addEpubFavorite(EpubBook _epubBook)
	public void addEpubFavorite(int _epub_id) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(Database.epubBook_id, _epub_id);

			db.insert(Database.TABLE_EPUB_FAVORITE, null, values);

		} finally {
			db.close();
		}

	}// end-func addEpubFavorite

	// start-func deleteEpubBook(int id)
	public int delEpubFavorite(int id) {
		int result = -1;

		db = dbhelper.getWritableDatabase();
		try {
			result = db.delete(Database.TABLE_EPUB_FAVORITE,
								Database.epubBook_id + "=?",
								new String[] { String.valueOf(id) });
			
		} finally {
			db.close();
		}
		return result;
	}// end-func deleteEpubBook

	// star-func loadAllEpubBook favorites
	public List<EpubBook> loadAllEpubBookFavorites() {
		List<EpubBook> list = new ArrayList<EpubBook>();
		db = dbhelper.getWritableDatabase();

		String sql = "SELECT " 
					+ "b." + Database.epubBook_id + ", " 
					+ "b." + Database.epubBookName + ", " 
					+ "b." + Database.epubBookAuthor + ", " 
					+ "b." + Database.epubBookFolder + ", " 
					+ "b." + Database.epubBookCover 
					+ " FROM "
					+ Database.TABLE_EPUB_FAVORITE + " AS a" 
					+ " LEFT JOIN "
					+ Database.TABLE_EPUB_BOOK + " AS b " 
					+ " ON " 
					+ "a." + Database.epubBook_id 
					+ "=" 
					+ "b." + Database.epubBook_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		try {
			while (!c.isAfterLast()) {
				EpubBook epubBook = new EpubBook();

				epubBook.setEpubBook_id(c.getInt(c
						.getColumnIndex(Database.epubBook_id)));
				epubBook.setEpubBookName(c.getString(c
						.getColumnIndex(Database.epubBookName)));
				epubBook.setEpubBookAuthor(c.getString(c
						.getColumnIndex(Database.epubBookAuthor)));
				epubBook.setEpubCover(c.getString(c
						.getColumnIndex(Database.epubBookCover)));
				epubBook.setEpubFolder(c.getString(c
						.getColumnIndex(Database.epubBookFolder)));

				list.add(epubBook);
				c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}

		return list;
	}// end-func loadAllEpubBook

	// star-func loadAllEpubBook
	public List<EpubFavorite> loadAllEpubFavorites() {
		List<EpubFavorite> list = new ArrayList<EpubFavorite>();
		db = dbhelper.getWritableDatabase();

		String sql = "SELECT " + Database.epubBook_id 
					+ " FROM "
					+ Database.TABLE_EPUB_FAVORITE;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		try {
			while (!c.isAfterLast()) {
				EpubFavorite epubBook = new EpubFavorite();

				epubBook.setEpubBook_id(Integer.parseInt(c.getString(c
						.getColumnIndex(Database.epubBook_id))));

				list.add(epubBook);
				c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}
		return list;
	}

	public void open() throws SQLException {
		db = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
		db.close();
	}
}
