package com.owo.android.mtPlease;

import android.app.Activity;
import android.app.Fragment;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewJavascriptInterface {
	private Activity mContext;
	private Fragment mFragment;

	/**
	 * 생성자.
	 * 
	 * @param activity : context
	 * @param view : 적용될 웹뷰
	 */
	public WebViewJavascriptInterface(Activity activity, Fragment fragment) {
		mContext = activity;
		mFragment = fragment;
	}

	/**
	 * 안드로이드 토스트를 출력한다. Time Long.
	 * 
	 * @param message : 메시지
	 */
	@JavascriptInterface
	public void toastLong(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 안드로이드 토스트를 출력한다. Time Short.
	 * 
	 * @param message : 메시지
	 */
	@JavascriptInterface
	public void toastShort(String message) { // Show toast for a short time
		//Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		((MainFragment) mFragment).toastShort(message);
	}
}
