package com.owo.android.mtPlease;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyPageFragment extends Fragment {

	private	WebView webViewMyPage;
	private WebViewJavascriptInterface mWebViewInterface;
	ProgressBar mProgressBar;
	View progressBarBackground;
	Drawable pbBackground;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myPageView = inflater.inflate(R.layout.fragment_info_mtplease, container,
				false);
		/*
		View myPageView = inflater.inflate(R.layout.fragment_mypage, container,
				false);
		
		mProgressBar = (ProgressBar) myPageView
				.findViewById(R.id.progressBar_mypageFragment);

		progressBarBackground = myPageView
				.findViewById(R.id.background_progressBar_mypageFragment);
		pbBackground = progressBarBackground.getBackground();
		pbBackground.setAlpha(0);

		webViewMyPage = (WebView) myPageView.findViewById(R.id.webViewMyPage);
		WebSettings webSettings = webViewMyPage.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webViewMyPage.setWebViewClient(new CustomWebViewClient(mProgressBar, pbBackground));
		webViewMyPage.setWebChromeClient(new WebChromeClient());
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), this);
		webViewMyPage.addJavascriptInterface(mWebViewInterface, "MyPageFragment");
		webViewMyPage.loadUrl("http://mtplease.herokuapp.com/members/myPage_m?user_id=" + getArguments().getString("SESSION_ID"));		
		Log.i("MyPageFragment - onCreateView", "loaded");
		 */
		// TODO Auto-generated method stub
		return myPageView;
	}
	/*
	public boolean getWebViewCanGoBack() {
		if(webViewMyPage != null)
			return webViewMyPage.canGoBack();
		else
			return false;
	}
	
	public void enableWebViewBack() {
			webViewMyPage.goBack();
	}
	*/
}
