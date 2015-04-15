package com.ken.ebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

import com.ken.ebook.R;

public class ActivitySplashScreen extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 3000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}// end-if
					}// end-while
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					Intent mainIntent	= new Intent(ActivitySplashScreen.this, ActivityMain.class);
					ActivitySplashScreen.this.startActivity(mainIntent);
					ActivitySplashScreen.this.finish();
				}// end-try
			}// end-run
		};
		splashTread.start();
	}// end-func onCreate

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}// end-if
		return true;
	}// end-func onTouchEvent
}