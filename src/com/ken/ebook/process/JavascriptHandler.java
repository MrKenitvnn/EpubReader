package com.ken.ebook.process;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ken.ebook.activity.ActivityReading;

public class JavascriptHandler {
	ActivityReading parentActivity;
	Context mContext;

	public JavascriptHandler(Context _Context) {
		mContext = _Context;
	}

	public void setResult(String val) {
		Log.v("mylog", "JavaScriptHandler.setResult is called : " + val);
		// this.parentActivity.javascriptCallFinished(val);
		Toast.makeText(mContext, val, Toast.LENGTH_SHORT).show();
	}

}// end-class JavaScriptHandler