package com.ken.ebook.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "ebookReader";
	private static int DATABASE_VERSION = 1;

	public static String TABLE_EPUB_BOOK = "tblEpubBook";
	public static String epubBook_id = "epubBookId";
	public static String epubBookName = "epubBookName";
	public static String epubBookAuthor = "epubBookAuthor";
	public static String epubBookCover = "epubBookCover";
	public static String epubBookFolder = "epubBookFolder";
	public static String epubBookFilePath = "epubBookFilePath";
	public static String epubBookContentFilePath = "epubBookContentFilePath";
	public static String epubBookNcxFilePath = "epubBookNcxFilePath";

	public static String TABLE_EPUB_BOOKMARK = "tblEpubBookmark";
	// public static String epubBook_id = "epubBookId";
	public static String bookmarkComponentId = "bookmarkComponentId";
	public static String bookmarkPercent = "bookmarkPercent";

	public static String TABLE_EPUB_CHAPTER = "tblEpubChapter";
	public static String chapterId = "chapterId";
	// public static String epubBook_id = "epubBookId";
	public static String chapterPath = "chapterPath";
	public static String chapterSrc = "chapterSrc";
	public static String chapterTitle = "chapterTitle";
	public static String chapterPlayOrder = "chapterPlayOrder";
	
	public static String TABLE_EPUB_CSS = "tblEpubCss";
	public static String cssId = "cssId";
	// public static String epubBook_id = "epubBookId";
	public static String cssPath = "cssPath";	

	public static String TABLE_EPUB_FAVORITE = "tblEpubFavorite";
	public static String favorite_id = "favoriteId";

	// public static String epubBook_id = "epubBookId";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tblEpubBook = " CREATE TABLE " + TABLE_EPUB_BOOK + " ( "
				+ epubBook_id
				+ " Integer primary key autoincrement, "
				+ epubBookName
				+ " text, "
				+ epubBookAuthor
				+ " text, "
				+ epubBookCover
				+ " text, "
				+ epubBookFolder
				+ " text, "
				+ epubBookFilePath
				+ " text, "
				+ epubBookContentFilePath
				+ " text, "
				+ epubBookNcxFilePath
				+ " text "
				+ ")";
		
		String tblBookmark = " CREATE TABLE " + TABLE_EPUB_BOOKMARK + " ( "
				+ epubBook_id
				+ " Integer primary key, "
				+ bookmarkComponentId
				+ " text, "
				+ bookmarkPercent
				+ " text "
				+ ")";
		
		String tblChapter = " CREATE TABLE " + TABLE_EPUB_CHAPTER + " ( "
				+ chapterId
				+ " Integer primary key autoincrement, "
				+ epubBook_id
				+ " integer, "
				+ chapterPath
				+ " text, "
				+ chapterSrc
				+ " text, "
				+ chapterTitle
				+ " text, "
				+ chapterPlayOrder
				+ " integer "		
				+ ")";
		String tblCss = " CREATE TABLE " + TABLE_EPUB_CSS + " ( "
				+ cssId
				+ " Integer primary key autoincrement, "
				+ epubBook_id
				+ " integer, "
				+ cssPath
				+ " text "
				+" ) ";
				
		
		String tblFavorite = " CREATE TABLE " + TABLE_EPUB_FAVORITE + " ( "
				+ favorite_id
				+ " Integer primary key autoincrement, "
				+ epubBook_id
				+ " integer "	
				+ ")";
		
		db.execSQL(tblEpubBook);
		db.execSQL(tblBookmark);
		db.execSQL(tblChapter);
		db.execSQL(tblFavorite);
		db.execSQL(tblCss);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("crop table if exists "+ TABLE_EPUB_BOOK);
		db.execSQL("crop table if exists "+ TABLE_EPUB_BOOKMARK);
		db.execSQL("crop table if exists "+ TABLE_EPUB_CHAPTER);
		db.execSQL("crop table if exists "+ TABLE_EPUB_FAVORITE);
		db.execSQL("crop table if exists "+ TABLE_EPUB_CSS);
		
		onCreate(db);
	}
}
