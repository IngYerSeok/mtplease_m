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

public class EstimateFragment extends Fragment {

	private WebView webViewEstimate;
	private WebViewJavascriptInterface mWebViewInterface;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View estimateView = inflater.inflate(R.layout.fragment_estimate, container,
				false);

		webViewEstimate = (WebView) estimateView.findViewById(R.id.webViewEstimate);
		WebSettings webSettings = webViewEstimate.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webViewEstimate.setWebViewClient(new WebViewClient() {

		});
		webViewEstimate.setWebChromeClient(new WebChromeClient() {

		});
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), this);
		webViewEstimate.addJavascriptInterface(mWebViewInterface, "EstimateFragment");
		webViewEstimate.loadUrl("file:///android_asset/pensions_estimate_add_barbecue_m.html");

		Log.i("EstimateFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return estimateView;
	}

	public boolean getWebViewCanGoBack() {
		if(webViewEstimate != null)
			return webViewEstimate.canGoBack();
		else
			return false;
	}
	
	public void enableWebViewBack() {
		if(webViewEstimate != null) {
			webViewEstimate.goBack();
		}
	}
}
