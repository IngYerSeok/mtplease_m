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

public class MainFragment extends Fragment {

	private WebView webViewMain;
	private WebViewJavascriptInterface mWebViewInterface;

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
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), webViewMain);
		webViewMain.addJavascriptInterface(mWebViewInterface, "MainFragment");
		webViewMain.loadUrl("http://mtplease.herokuapp.com/pensions/search_m?people=20&region=1&date=2014-11-15&flag=1");
		//webViewMain.loadUrl("file:///android_asset/pensions_resultList_m.html");

		Log.i("MainFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return mainView;
	}
	
	public void refreshPage(String url) {
		webViewMain.loadUrl("http://mtplease.herokuapp.com/pensions/search_m"
				+ url);
	}
	
	public boolean getWebViewCanGoBack() {
		if(webViewMain != null)
			return webViewMain.canGoBack();
		else
			return false;
	}
	
	public void enableWebViewBack() {
		if(webViewMain != null)
			webViewMain.goBack();
	}
}
