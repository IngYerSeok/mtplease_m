package com.owo.android.mtPlease;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainFragment extends Fragment {

	WebView webViewMain;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_main, container,
				false);

		webViewMain = (WebView) mainView.findViewById(R.id.webViewMain);
		WebSettings webSettings = webViewMain.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		webViewMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		webViewMain.setWebViewClient(new WebViewClient() {

		});
		webViewMain.setWebChromeClient(new WebChromeClient() {

		});

		webViewMain.loadUrl("http://mtplease.herokuapp.com/pensions/search_m?people=20&region=1&date=2014-11-15&flag=1");

		Log.i("MainFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return mainView;
	}
	
	public void refreshPage(String url) {
		webViewMain.loadUrl("http://mtplease.herokuapp.com/pensions/search_m"
				+ url);
	}

}
