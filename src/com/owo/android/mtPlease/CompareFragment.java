package com.owo.android.mtPlease;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CompareFragment extends Fragment {

	private WebView webViewCompare;
	private WebViewJavascriptInterface mWebViewInterface;
	ProgressBar mProgressBar;
	View progressBarBackground;
	Drawable pbBackground;
	private String user_id;
	private String rooms;
	private String deletedRoom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View compareView = inflater.inflate(R.layout.fragment_compare,
				container, false);
		user_id = getArguments().getString("SESSION_ID");

		mProgressBar = (ProgressBar) compareView
				.findViewById(R.id.progressBar_compareFragment);

		progressBarBackground = compareView
				.findViewById(R.id.background_progressBar_compareFragment);
		pbBackground = progressBarBackground.getBackground();
		pbBackground.setAlpha(0);

		webViewCompare = (WebView) compareView
				.findViewById(R.id.webViewCompare);
		WebSettings webSettings = webViewCompare.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webViewCompare.setWebViewClient(new CustomWebViewClient(mProgressBar,
				pbBackground));
		webViewCompare.setWebChromeClient(new WebChromeClient());
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), this);
		webViewCompare.addJavascriptInterface(mWebViewInterface,
				"CompareFragment");
		webViewCompare
				.loadUrl("http://mtplease.herokuapp.com/pensions/compare_m?user_id="
						+ user_id);
		// webViewCompare.loadUrl("http://mtplease.herokuapp.com/pensions/compare_m");

		Log.i("CompareFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return compareView;
	}

	public boolean getWebViewCanGoBack() {
		if (webViewCompare != null)
			return webViewCompare.canGoBack();
		else
			return false;
	}

	public void enableWebViewBack() {
		if (webViewCompare != null)
			webViewCompare.goBack();
	}

	public void addCompareList(String rooms) {
		this.rooms = rooms;
		Log.i("addcompare", this.rooms);
		startAddCompare();

	}
	
	public void setDeletedRoom(String deletedRoom) {
		this.deletedRoom = deletedRoom;
		Log.i("compare delete room", deletedRoom);
		startDeletecompare();
	}

	private void startDeletecompare() {
		DeleteCompareTask mDeleteCompareTask = new DeleteCompareTask(
				new FragmentCallback() {

					@Override
					public void onTaskDone(boolean isLoginSuccess,
							String emailAddress) {
					};

					@Override
					public void onAddCompareTaskDone(boolean isAddCompareSuccess) {
						webViewCompare
								.loadUrl("http://mtplease.herokuapp.com/pensions/compare_m?user_id="
										+ user_id);
					}
					
					@Override
					public void onDeleteCompareTaskDone(boolean isDeleteCompareSuccess) {
						Toast.makeText(getActivity(), "펜션 정보가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onAddEstimateTaskDone(
							boolean isAddEstimateSuccess) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onDeleteEstimateTaskDone(
							boolean isDeleteEstimateSuccess) {
						// TODO Auto-generated method stub
						
					}

				});
		mDeleteCompareTask.execute();
	}

	public void startAddCompare() {
		AddCompareTask mAddCompareTask = new AddCompareTask(
				new FragmentCallback() {

					@Override
					public void onTaskDone(boolean isLoginSuccess,
							String emailAddress) {
					};

					@Override
					public void onAddCompareTaskDone(boolean isAddCompareSuccess) {
						webViewCompare
								.loadUrl("http://mtplease.herokuapp.com/pensions/compare_m?user_id="
										+ user_id);
					}

					@Override
					public void onAddEstimateTaskDone(
							boolean isAddEstimateSuccess) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onDeleteCompareTaskDone(
							boolean isDeleteCompareSuccess) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onDeleteEstimateTaskDone(
							boolean isDeleteEstimateSuccess) {
						// TODO Auto-generated method stub
						
					}

				});
		mAddCompareTask.execute();
	}

	// AsyncTask<Params,Progress,Result>
	private class AddCompareTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		private boolean isAddCompareSuccess = false;
		private JSONObject o;

		public AddCompareTask(FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {

			HttpPost httppost = new HttpPost(
					"http://mtplease.herokuapp.com/pensions/compare_m");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Log.i("rooms", rooms);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs.add(new BasicNameValuePair("rooms", rooms));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"utf-8"));
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				o = new JSONObject(result.toString());
				Log.i("???why?", o.get("result").toString());

				if (o.get("result").toString() == "true") {
					isAddCompareSuccess = true;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			// super.onPostExecute(result);
			try {
				mFragmentCallback.onAddCompareTaskDone(isAddCompareSuccess);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
	
	// AsyncTask<Params,Progress,Result>
	private class DeleteCompareTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		private boolean isDeleteCompareSuccess = false;
		private JSONObject o;

		public DeleteCompareTask(FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {

			HttpPost httppost = new HttpPost(
					"http://mtplease.herokuapp.com/pensions/compare_m/delete");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs.add(new BasicNameValuePair("rooms", deletedRoom));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"utf-8"));
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				o = new JSONObject(result.toString());
				Log.i("???why?", o.get("result").toString());

				if (o.get("result").toString() == "true") {
					isDeleteCompareSuccess = true;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			// super.onPostExecute(result);
			try {
				mFragmentCallback.onDeleteCompareTaskDone(isDeleteCompareSuccess);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
}
