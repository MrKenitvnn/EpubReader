package com.ken.ebook.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.EpubChapter;

public class JsoupParse {
	private static String bookName;
	private static String bookAuthor;

	private static String chapterTitle;
	private static String chapterSrc;
	private static String chapterPath;
	private static String chapterFile;
	private static String chapterPlayOrder;

	static EpubBook book;

	public static EpubBook epubBookData(String contentFilePath) {
		book = new EpubBook();
		File contentFile = new File(contentFilePath);

		try {
			Document contentDoc = Jsoup.parse(contentFile, "UTF-8");

			bookName = contentDoc.getElementsByTag("dc:title").first().text();
			bookAuthor = contentDoc.getElementsByTag("dc:creator").first()
					.text();

			book.setEpubBookName(bookName).setEpubBookAuthor(bookAuthor)
					.setContentFilePath(contentFilePath);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return book;
	}

	public static List<EpubChapter> listEpubChapterData(EpubBook book) {
		String tocNcxPath = book.getNcxFilePath();
		String contentPath = book.getContentFilePath();
		String book_id = book.getEpubFolder();
		String epubFileFolder = book.getEpubFilePath();
		List<EpubChapter> lstChapter = new ArrayList<EpubChapter>();
		File tocFile = new File(tocNcxPath);

		try {
			Document tocDoc = Jsoup.parse(tocFile, "UTF-8");

			Elements navPoint = tocDoc.select("navPoint");

			for (Element el : navPoint) {
				chapterTitle = el.getElementsByTag("navLabel").first().text();
				chapterSrc = el.getElementsByTag("content").attr("src")
						.toString().replace("\\", "/").trim();
				chapterPlayOrder = el.attr("playOrder").toString();
				chapterFile = FileHandler.getLastTokenizer(chapterSrc, "/");
				chapterPath = FileHandler.getChapterPath(new File(
						epubFileFolder), chapterFile);

				lstChapter.add(new EpubChapter()
						.setEpubBook_id(Integer.parseInt(book_id))
						.setChapterTitle(chapterTitle)
						.setChapterSrc(chapterSrc)
						.setPlayOrder(Integer.parseInt(chapterPlayOrder))
						.setChapterPath(chapterPath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstChapter;
	}

	public static String getChapterComponent(String chapterPath) {
		String result = "";
		HtmlCleaner htmCleaner = new HtmlCleaner();
		TagNode baseResultNode = null;
		try {
			baseResultNode = htmCleaner.clean(new File(chapterPath));

			TagNode chanNode = null;
			chanNode = baseResultNode.findElementByName("body", false);
			result = htmCleaner.getInnerHtml(chanNode).trim();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
