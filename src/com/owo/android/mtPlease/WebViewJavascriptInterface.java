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
	 * ������.
	 * 
	 * @param activity : context
	 * @param view : ����� ����
	 */
	public WebViewJavascriptInterface(Activity activity, Fragment fragment) {
		mContext = activity;
		mFragment = fragment;
	}

	/**
	 * �ȵ���̵� �佺Ʈ�� ����Ѵ�. Time Long.
	 * 
	 * @param message : �޽���
	 */
	@JavascriptInterface
	public void toastLong(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * �ȵ���̵� �佺Ʈ�� ����Ѵ�. Time Short.
	 * 
	 * @param message : �޽���
	 */
	@JavascriptInterface
	public void toastShort(String message) { // Show toast for a short time
		//Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		((MainFragment) mFragment).toastShort(message);
	}
}
