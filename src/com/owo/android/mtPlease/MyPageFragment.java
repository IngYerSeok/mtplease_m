package com.owo.android.mtPlease;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.owo.android.mtPlease.LoginPageFragment.FragmentCallback;

import android.app.Fragment;
import android.os.AsyncTask;
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

	private	WebView webViewMyPage;
	private WebViewJavascriptInterface mWebViewInterface;

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
		webViewMyPage.addJavascriptInterface(mWebViewInterface, "MyPageFragment");
		webViewMyPage.loadUrl("http://mtplease.herokuapp.com/members/myPage_m");
		
		Log.i("MyPageFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return myPageView;
	}
	
	public void loadMyPage(String emailAddress){
		webViewMyPage.loadUrl("http://mtplease.herokuapp.com/members/myPage_m?" + emailAddress);
		// new LoadMyPage(emailAddress).execute();

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
