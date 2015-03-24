package com.ken.ebook.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ken.ebook.R;
import com.ken.ebook.DAO.EpubBookDAO;
import com.ken.ebook.DAO.EpubBookmarkDAO;
import com.ken.ebook.DAO.EpubChapterDAO;
import com.ken.ebook.DAO.EpubCssDAO;
import com.ken.ebook.model.EpubBook;
import com.ken.ebook.model.EpubBookmark;
import com.ken.ebook.model.EpubChapter;
import com.ken.ebook.model.EpubCss;
import com.ken.ebook.process.FileHandler;
import com.ken.ebook.process.JsoupParse;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class ActivityReading extends Activity {
	public static int test = 0x0;
	static int book_id;
	String chapterSrc;
	int state_show = 0x0;
	String webData = "";

	WebView webview;
	Button btnBookmark;
	Button btnShowControl;
	RelativeLayout relHoldControl;
	EpubBookDAO bookDAO;
	EpubBookmarkDAO bookmarkDAO;
	static EpubChapterDAO chapterDAO;
	static EpubCssDAO cssDAO;
	static List<EpubChapter> listChapter;
	static List<EpubCss> listCss;
	EpubBookmark mBookmark;
	public static ProgressDialog pd;

	@SuppressLint({ "JavascriptInterface", "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readding);

		bookDAO = new EpubBookDAO(getApplicationContext());
		bookmarkDAO = new EpubBookmarkDAO(getApplicationContext());
		chapterDAO = new EpubChapterDAO(getApplicationContext());
		cssDAO = new EpubCssDAO(getApplicationContext());

		// init controls
		webview = (WebView) findViewById(R.id.webkit);
		btnBookmark = (Button) findViewById(R.id.btnBookmark);
		btnShowControl = (Button) findViewById(R.id.btnShowControl);
		relHoldControl = (RelativeLayout) findViewById(R.id.relHoldControl);

		// event click
		btnShowControl.setOnClickListener(viewShowControl);
		btnBookmark.setOnClickListener(bookMark);

		// Nhan du lieu tu ben ben kia truyen sang
		EpubBook book = (EpubBook) getIntent().getExtras().getSerializable(
				"BOOK");
		book_id = Integer.parseInt(book.getEpubFolder());

		// base data
		listChapter = new ArrayList<EpubChapter>();
		listChapter.addAll(chapterDAO.getListChapterByBookId(book_id));
		listCss = new ArrayList<EpubCss>();
		listCss.addAll(cssDAO.getListCssByBookId(book_id));
		mBookmark = bookmarkDAO.getEpubBookmarkById(book_id);

		// đọc file script từ assets
		String script = getAsset("finalebookscript.js");
		String script2 = getAsset("finalebookscript2.js");

		// book data
		String bookData = "var bookData = {" + "getComponents: function () {"
				+ "return [";
		int i = 0;
		for (EpubChapter item : listChapter) {
			bookData += "'" + item.getChapterSrc().trim() + "'";
			bookData += i < listChapter.size() - 1 ? "," : "";
			i++;
		}
		bookData += "];" + "}," + "getContents: function () {" + "return [";
		i = 0;
		for (EpubChapter item : listChapter) {
			bookData += "{title:\"" + item.getChapterTitle().trim() + "\",";
			bookData += "src: \"" + item.getChapterSrc().trim() + "\"}";
			bookData += i < listChapter.size() - 1 ? "," : "";
			i++;
		}
		bookData += "]" + "}," + "getComponent: function (componentId) {"
				+ "return {";
		i = 0;
		for (EpubChapter item : listChapter) {
			bookData += "'" + item.getChapterSrc().trim() + "':";
			bookData += "'"
					+ JsoupParse.getChapterComponent(item.getChapterPath())
							.replace("\n", "").replace("<a", "<p")
							.replace("<a>", "<p>").trim() + "'";
			bookData += i < listChapter.size() - 1 ? "," : "";

			i++;
		}

		bookData += "}[componentId];" + "}," + "getMetaData: function(key) {"
				+ "return {" + "title: \"" + book.getEpubBookName() + "\","
				+ "creator: \"" + book.getEpubBookAuthor() + "\"" + "}[key];"
				+ "}" + "}";

		// end book data

		// start - web data
		webData = "<!DOCTYPE html>"
				+ "<html>"
				+ "<head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
				+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">"
				+ "<meta"
				+ "name=\"viewport\""
				+ "content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\""
				+ "/>"
				+ "<title></title>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/monocore.css\" />"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/monoctrl.css\" />";

		for (EpubCss item : listCss) {
			webData += "<link rel=\"stylesheet\" type=\"text/css\" href=\""
					+ item.getCssPath().trim() + "\" />";
		}

		webData += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/finalebookstyle.css\" />"
				+ "<script type=\"text/javascript\" src=\"file:///android_asset/monocore.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"file:///android_asset/monoctrl.js\"></script>"
				+ "<script>" // script start
				+ bookData + script;

		if (mBookmark != null) {
			webData += " var locus = {componentId: '"
					+ mBookmark.getComponentId() + "',percent: '"
					+ mBookmark.getPercent() + "'};";
			webData += " reader.moveTo(locus);";
		}

		webData += script2 + " </script>" // end script
				+ "</head>";

		webData += "" + "<body>" + "<div id=\"readerBg\">"
				+ "<div class=\"board\"></div>"
				+ "<div class=\"jacket\"></div>"
				+ "<div class=\"dummyPage\"></div>"
				+ "<div class=\"dummyPage\"></div>"
				+ "<div class=\"dummyPage\"></div>"
				+ "<div class=\"dummyPage\"></div>"
				+ "<div class=\"dummyPage\"></div>"
				+ "<div class=\"dummyPage\"></div>" + "</div>" + ""
				+ "<div id=\"readerCntr\">"
				+ "<div class=\"reader\" id=\"reader\"></div>" + "</div>";

		webData += "<script type=\"text/javascript\" src=\"file:///android_asset/finalebookscript.js\"></script>";

		webData += "</body>" + "</html>";

	} // end-func onCreate

	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setAllowFileAccessFromFileURLs(true);
		webview.setWebViewClient(new WebViewClient());
		webview.setWebChromeClient(new WebChromeClient());
		webview.addJavascriptInterface(this, "android");
		webview.getSettings().setUseWideViewPort(false);
		webview.requestFocusFromTouch();
		// these settings speed up page load into the webview
		webview.getSettings().setRenderPriority(RenderPriority.HIGH);
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webview.requestFocus(View.FOCUS_DOWN);
		// ghi vào file của folder data trong sdcard
		FileHandler.writeData(webData, "index.html");

		webview.loadUrl("file://" + FileHandler.rootPath
				+ FileHandler.DATA_FOLDER + "/index.html");
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		webview.loadUrl("javascript:getComponentId()");
	} // end-func onPause

	// get asset
	public String getAsset(String file_name) {
		// đọc file script từ assets
		StringBuilder stbScript = new StringBuilder();
		InputStream isScript;
		try {
			isScript = getAssets().open(file_name);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					isScript, "UTF-8"));
			String str;

			while ((str = br.readLine()) != null) {
				stbScript.append(str + "\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stbScript.toString();
	}// end-func getAsset

	@JavascriptInterface
	public void setData(String componentId, String percentId) {
		if (mBookmark != null) {
			mBookmark.setComponentId(componentId).setPercent(percentId);
			bookmarkDAO.editEpubBookmark(mBookmark);
			Log.d("mylog edit", componentId + " - " + percentId);
		} else {
			mBookmark = new EpubBookmark(book_id, componentId, percentId);
			bookmarkDAO.addEpubBookmark(mBookmark);
			Log.d("mylog add", componentId + " - " + percentId);
		}
	}// end-func setData

	// event
	View.OnClickListener viewShowControl = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (state_show) {
			case 0x0:
				relHoldControl.setVisibility(View.VISIBLE);
				Animation shake = AnimationUtils.loadAnimation(
						ActivityReading.this, R.anim.slide_top_to_bottom);
				findViewById(R.id.relHoldControl).startAnimation(shake);
				state_show = 0x1;
				break;

			case 0x1:
				Animation out = AnimationUtils.loadAnimation(
						ActivityReading.this, R.anim.push_up_out);
				findViewById(R.id.relHoldControl).startAnimation(out);
				relHoldControl.setVisibility(View.GONE);
				state_show = 0x0;
				break;

			default:
				break;
			}

		}
	};

	View.OnClickListener bookMark = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			webview.loadUrl("javascript:getComponentId()");
		}
	};

	// end-event
}
