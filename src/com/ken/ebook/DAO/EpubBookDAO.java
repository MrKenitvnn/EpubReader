package com.ken.ebook.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ken.ebook.model.EpubBook;

public class EpubBookDAO {
	static Database dbhelper;
	static SQLiteDatabase db;
	Context context;

	public EpubBookDAO(Context context) {
		this.context = context;
		dbhelper = new Database(context);
	}

	// start-func addEpubBook(EpubBook _epubBook)
	public void addEpubBook(EpubBook _epubBook) {
		db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Database.epubBookName, _epubBook.getEpubBookName());
			values.put(Database.epubBookAuthor, _epubBook.getEpubBookAuthor());
			values.put(Database.epubBookCover, _epubBook.getEpubCover());
			values.put(Database.epubBookFolder, _epubBook.getEpubFolder());
			values.put(Database.epubBookFilePath, _epubBook.getEpubFilePath());
			values.put(Database.epubBookContentFilePath,
					_epubBook.getContentFilePath());
			values.put(Database.epubBookNcxFilePath, _epubBook.getNcxFilePath());

			db.insert(Database.TABLE_EPUB_BOOK, null, values);
		} finally {
			db.close();
		}

	}// end-func addEpubBook

	// // start-func editEpubBook(EpubBook _epubBook)
	// public int editEpubBook(EpubBook _epubBook) {
	// db = dbhelper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put(Database.epubBookName, _epubBook.getEpubBookName());
	// values.put(Database.epubBookAuthor, _epubBook.getEpubBookAuthor());
	// values.put(Database.epubBookCover, _epubBook.getEpubCover());
	// values.put(Database.epubBookFolder, _epubBook.getEpubFolder());
	// values.put(Database.epubBookContentFilePath,
	// _epubBook.getContentFilePath());
	// values.put(Database.epubBookNcxFilePath, _epubBook.getNcxFilePath());
	//
	// int result = db.update(Database.TABLE_EPUB_BOOK, values,
	// Database.epubBook_id + "=?",
	// new String[] { String.valueOf(_epubBook.getEpubBook_id()) });
	// return result;
	//
	// }// end-func editEpubBook

	// start-func delEpubBook(int id)
	public int delEpubBook(int id) {
		int result = -1;
		db = dbhelper.getWritableDatabase();
		try {
			result = db.delete(Database.TABLE_EPUB_BOOK, Database.epubBook_id
					+ "=" + id, null);
		} finally {
			db.close();
		}
		return result;
	}// end-func delEpubBook

	// start-func getEpubBookById(int id)
	public EpubBook getEpubBookById(int id) {
		db = dbhelper.getReadableDatabase();

		String sql = "select * from " + Database.TABLE_EPUB_BOOK + " where "
				+ Database.TABLE_EPUB_BOOK + "." + Database.epubBook_id + " = "
				+ id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		EpubBook epubBook = new EpubBook();

		try {
			while (!c.isAfterLast()) {
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
				epubBook.setEpubFilePath(c.getString(c
						.getColumnIndex(Database.epubBookFilePath)));
				epubBook.setContentFilePath(c.getString(c
						.getColumnIndex(Database.epubBookContentFilePath)));
				epubBook.setNcxFilePath(c.getString(c
						.getColumnIndex(Database.epubBookNcxFilePath)));

				c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}

		return epubBook;
	}// end-func getEpubBookById

	public int getId(String bookName, String bookAuthor) {
		int id = -1;

		return id;
	}

	// star-func getAnEpubBook
	public int getLastId() {
		int id = 0;
		db = dbhelper.getWritableDatabase();

		String query = "select MAX(" + Database.epubBook_id + ") from "
				+ Database.TABLE_EPUB_BOOK;

		Cursor c = db.rawQuery(query, null);
		try {
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				id = c.getInt(0);
			}
		} finally {
			c.close();
			db.close();
		}

		return id;
	}

	// star-func loadAllEpubBook
	public List<EpubBook> loadAllEpubBook() {
		List<EpubBook> list = new ArrayList<EpubBook>();
		db = dbhelper.getReadableDatabase();
		String sql = "select * from " + Database.TABLE_EPUB_BOOK;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		try {
			while (!c.isAfterLast()) {
				EpubBook epubBook = new EpubBook();
				epubBook.setEpubBook_id(Integer.parseInt(c.getString(c
						.getColumnIndex(Database.epubBook_id))));
				epubBook.setEpubBookName(c.getString(c
						.getColumnIndex(Database.epubBookName)));
				epubBook.setEpubBookAuthor(c.getString(c
						.getColumnIndex(Database.epubBookAuthor)));
				epubBook.setEpubCover(c.getString(c
						.getColumnIndex(Database.epubBookCover)));
				epubBook.setEpubFolder(c.getString(c
						.getColumnIndex(Database.epubBookFolder)));
				epubBook.setEpubFilePath(c.getString(c
						.getColumnIndex(Database.epubBookFilePath)));
				epubBook.setContentFilePath(c.getString(c
						.getColumnIndex(Database.epubBookContentFilePath)));
				epubBook.setNcxFilePath(c.getString(c
						.getColumnIndex(Database.epubBookNcxFilePath)));

				list.add(epubBook);
				c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}
		return list;
	}// end-func loadAllEpubBook

	public boolean checkABook(EpubBook book) {
		boolean result = false;
		db = dbhelper.getWritableDatabase();
		String bookName = book.getEpubBookName();
		String bookAuthor = book.getEpubBookAuthor();

		String sql = "select * from " + Database.TABLE_EPUB_BOOK + " where "
				+ Database.TABLE_EPUB_BOOK + "." + Database.epubBookName + "='"
				+ bookName + "' AND " + Database.TABLE_EPUB_BOOK + "."
				+ Database.epubBookAuthor + "='" + bookAuthor + "'";

		Cursor c = db.rawQuery(sql, null);
		try {
			if (c != null && c.getCount() > 0) {
				result = true;
			}
		} finally {
			c.close();
			db.close();
		}
		return result;
	}

	public static void close() {
		db.close();
	}
}
