package com.owo.android.mtPlease;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
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
	
	@JavascriptInterface
	public void addCompareList(String rooms){
		Log.i("from jquery", rooms);
		((MainFragment) mFragment).addCompareList(rooms);
	}
	
	@JavascriptInterface
	public void addEstimateRoom(String rooms) {
		((MainFragment) mFragment).addEstimateRoom(rooms);
	}

	@JavascriptInterface
	public void addEstimateAlcohol(String alcohol) {
		((EstimateFragment) mFragment).addEstimateAlcohol(alcohol);
	}

	@JavascriptInterface
	public void addEstimateBarbecue(String barbecue) {
		((EstimateFragment) mFragment).addEstimateBarbecue(barbecue);
	}

	@JavascriptInterface
	public void addEstimateOther(String other) {
		((EstimateFragment) mFragment).addEstimateOther(other);
	}
}
