package com.owo.android.mtPlease;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class CustomWebViewClient extends WebViewClient {

	ProgressBar mProgressBar;
	Drawable mBackground;

	public CustomWebViewClient(ProgressBar indeterminateProgressBar, Drawable background) {
		mProgressBar = indeterminateProgressBar;
		mBackground = background;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.setProgress(0);
		mBackground.setAlpha(95);
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		mProgressBar.setVisibility(View.GONE);
		mProgressBar.setProgress(100);
		mBackground.setAlpha(0);
		super.onPageFinished(view, url);
	}

}
