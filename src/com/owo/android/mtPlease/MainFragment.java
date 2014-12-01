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
import android.widget.Toast;

public class MainFragment extends Fragment {

	private WebView webViewMain;
	private WebViewJavascriptInterface mWebViewInterface;
	ProgressBar mProgressBar;
	View progressBarBackground;
	Drawable pbBackground;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mProgressBar = (ProgressBar) mainView
				.findViewById(R.id.progressBar_mainFragment);

		progressBarBackground = mainView
				.findViewById(R.id.background_progressBar_mainFragment);
		pbBackground = progressBarBackground.getBackground();
		pbBackground.setAlpha(0);

		webViewMain = (WebView) mainView.findViewById(R.id.webViewMain);
		WebSettings webSettings = webViewMain.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webViewMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webViewMain.setWebViewClient(new CustomWebViewClient(mProgressBar,
				pbBackground));
		webViewMain.setWebChromeClient(new WebChromeClient());
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), this);
		webViewMain.addJavascriptInterface(mWebViewInterface, "MainFragment");
		webViewMain
				.loadUrl("http://mtplease.herokuapp.com/pensions/search_m?people=20&region=1&date=2014-11-15&flag=1");
		// webViewMain.loadUrl("file:///android_asset/pensions_resultList_m.html");

		Log.i("MainFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return mainView;
	}

	public void refreshPage(String url) {
		webViewMain.loadUrl("http://mtplease.herokuapp.com/pensions/search_m"
				+ url);
	}

	public boolean getWebViewCanGoBack() {
		if (webViewMain != null)
			return webViewMain.canGoBack();
		else
			return false;
	}

	public void enableWebViewBack() {
		if (webViewMain != null)
			webViewMain.goBack();
	}

	public void addCompareList(String rooms) {
		Log.i("addcompare", rooms);
		((MainActivity) getActivity()).sendInfoToFragment(rooms, 1);

	}

	public void addEstimateRoom(String rooms) {
		// TODO Auto-generated method stub
		((MainActivity)getActivity()).sendInfoToFragment(rooms, 2); 
	}
}
