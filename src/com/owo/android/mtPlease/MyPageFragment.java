package com.owo.android.mtPlease;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyPageFragment extends Fragment {

	WebView webViewMyPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myPageView = inflater.inflate(R.layout.fragment_mypage, container,
				false);

		
		webViewMyPage = (WebView) myPageView.findViewById(R.id.webViewMyPage);
		WebSettings webSettings = webViewMyPage.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webViewMyPage.setWebViewClient(new WebViewClient() {

		});
		webViewMyPage.setWebChromeClient(new WebChromeClient() {

		});

		webViewMyPage.loadUrl("http://mtplease.herokuapp.com/members/myPage_m");
		
		Log.i("MyPageFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return myPageView;
	}
	
	public void loadMyPage(String emailAddress){
		
	}

	public boolean getWebViewCanGoBack() {
		if(webViewMyPage != null)
			return webViewMyPage.canGoBack();
		else
			return false;
	}
	
	public void enableWebViewBack() {
			webViewMyPage.goBack();
	}
}
