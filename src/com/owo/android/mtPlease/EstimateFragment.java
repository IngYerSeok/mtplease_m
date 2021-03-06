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
import android.widget.ProgressBar;
import android.widget.Toast;

public class EstimateFragment extends Fragment {

	private WebView webViewEstimate;
	private WebViewJavascriptInterface mWebViewInterface;
	ProgressBar mProgressBar;
	View progressBarBackground;
	Drawable pbBackground;
	private String user_id;
	private int people=20;

	private String stringifiedAlcoholData;
	private String stringifiedBarbecueData;
	private String stringifiedOthersData;

	final static int ALCOHOL = 0;
	final static int BARBECUE = 1;
	final static int OTHERS = 2;
	
	private String deletedRoom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View estimateView = inflater.inflate(R.layout.fragment_estimate,
				container, false);
		user_id = getArguments().getString("SESSION_ID");

		mProgressBar = (ProgressBar) estimateView
				.findViewById(R.id.progressBar_estimateFragment);

		progressBarBackground = estimateView
				.findViewById(R.id.background_progressBar_estimateFragment);
		pbBackground = progressBarBackground.getBackground();
		pbBackground.setAlpha(0);

		webViewEstimate = (WebView) estimateView
				.findViewById(R.id.webViewEstimate);
		WebSettings webSettings = webViewEstimate.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webViewEstimate.setWebViewClient(new CustomWebViewClient(mProgressBar,
				pbBackground));
		webViewEstimate.setWebChromeClient(new WebChromeClient());
		mWebViewInterface = new WebViewJavascriptInterface(getActivity(), this);
		webViewEstimate.addJavascriptInterface(mWebViewInterface,
				"EstimateFragment");
		webViewEstimate
				.loadUrl("http://mtplease.herokuapp.com/pensions/estimate_m?user_id="
						+ user_id);
		// webViewEstimate.loadUrl("file:///android_asset/pensions_estimate_add_barbecue_m.html");

		Log.i("EstimateFragment - onCreateView", "loaded");

		// TODO Auto-generated method stub
		return estimateView;
	}

	public boolean getWebViewCanGoBack() {
		if (webViewEstimate != null)
			return webViewEstimate.canGoBack();
		else
			return false;
	}

	public void enableWebViewBack() {
		if (webViewEstimate != null) {
			webViewEstimate.goBack();
		}
	}
	
	public void loadEstimate() {
		// TODO Auto-generated method stub
		new LoadOverallEstimateTask().execute();
	}
	
	// AsyncTask<Params,Progress,Result>
	private class LoadOverallEstimateTask extends
			AsyncTask<String, Void, HttpResponse> {
		
		@Override
		protected HttpResponse doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(HttpResponse result) {
			// super.onPostExecute(result);
			try {
				webViewEstimate
				.loadUrl("http://mtplease.herokuapp.com/pensions/estimate_m/overall?user_id="
						+ user_id + "&people=" + people);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	public void addEstimateRoom(String room) {
		Log.i("estimate room", room);
		startAddEstimate(1, room);
		
	}

	public void addEstimateAlcohol(String alcohol) {
		Log.i("estimate alcol", alcohol);
		startAddEstimate(2, alcohol);
		clearWebViewHistory();
	}

	public void addEstimateBarbecue(String barbecue) {
		Log.i("estimate bbq", barbecue);
		startAddEstimate(3, barbecue);
		clearWebViewHistory();
	}

	public void addEstimateOther(String other) {
		Log.i("estimate other", other);
		startAddEstimate(4, other);
		clearWebViewHistory();
	}

	public void setDeletedRoom(String deletedRoom) {
		Log.i("delete estimate room", deletedRoom);
		startDeleteEstimate(1, deletedRoom);
		clearWebViewHistory();
	}
	
	private void clearWebViewHistory() {
	      webViewEstimate.clearHistory();
	   }

	public void setDeletedAlcohol(String alcohol) {
		Log.i("delete estimate alcol", alcohol);
		startDeleteEstimate(2, alcohol);
	}

	public void setDeletedBarbecue(String barbecue) {
		Log.i("delete estimate bbq", barbecue);
		startDeleteEstimate(3, barbecue);
	}

	public void setDeletedOther(String other) {
		Log.i("delete estimate other", other);
		startDeleteEstimate(4, other);
	}
	
	private void startDeleteEstimate(int flag, String string) {
		// TODO Auto-generated method stub
		DeleteEstimateTask mDeleteEstimateTask = new DeleteEstimateTask(flag, string,
				new FragmentCallback() {

					@Override
					public void onTaskDone(boolean isLoginSuccess,
							String emailAddress) {
					};

					@Override
					public void onAddCompareTaskDone(boolean isAddCompareSuccess) {
					};

					@Override
					public void onAddEstimateTaskDone(
							boolean isAddEstimateSuccess) {	};

					@Override
					public void onDeleteCompareTaskDone(
							boolean isDeleteCompareSuccess) {
						// TODO Auto-generated method stub
						
					};
					
					@Override
					public void onDeleteEstimateTaskDone(boolean isDeleteEstimateSuccess) {
						Toast.makeText(getActivity(), "정상적으로 견적 정보가 삭제되었습니다", Toast.LENGTH_SHORT).show();
					}
				});
		mDeleteEstimateTask.execute();
	}

	public void startAddEstimate(int flag, String string) {
		AddEstimateTask mAddEstimateTask = new AddEstimateTask(flag, string,
				new FragmentCallback() {

					@Override
					public void onTaskDone(boolean isLoginSuccess,
							String emailAddress) {
					};

					@Override
					public void onAddCompareTaskDone(boolean isAddCompareSuccess) {
					};

					@Override
					public void onAddEstimateTaskDone(
							boolean isAddEstimateSuccess) {
						webViewEstimate
								.loadUrl("http://mtplease.herokuapp.com/pensions/estimate_m?user_id="
										+ user_id);
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
		mAddEstimateTask.execute();
	}

	// AsyncTask<Params,Progress,Result>
	private class AddEstimateTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		private boolean isAddEstimateSuccess = false;
		private JSONObject o;
		private int flag;
		private String message;

		public AddEstimateTask(int flag, String sendmessage,
				FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
			this.flag = flag;
			this.message = sendmessage;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpPost httppost = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			switch (flag) {
			case 1: {
				nameValuePairs.add(new BasicNameValuePair("room", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/room");
				break;
			}

			case 2: {
				nameValuePairs.add(new BasicNameValuePair("alcohol", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/alcohol");
				break;
			}
			case 3: {
				nameValuePairs.add(new BasicNameValuePair("barbecue", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/barbecue");
				break;
			}
			case 4: {
				nameValuePairs.add(new BasicNameValuePair("other", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/other");
				break;
			}
			}
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
					isAddEstimateSuccess = true;
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
				mFragmentCallback.onAddEstimateTaskDone(isAddEstimateSuccess);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
	
	// AsyncTask<Params,Progress,Result>
	private class DeleteEstimateTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		private boolean isDeleteEstimateSuccess = false;
		private JSONObject o;
		private int flag;
		private String message;

		public DeleteEstimateTask(int flag, String sendmessage,
				FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
			this.flag = flag;
			this.message = sendmessage;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpPost httppost = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			switch (flag) {
			case 1: {
				nameValuePairs.add(new BasicNameValuePair("room", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/room/delete");
				break;
			}

			case 2: {
				nameValuePairs.add(new BasicNameValuePair("alcohol", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/alcohol/delete");
				break;
			}
			case 3: {
				nameValuePairs.add(new BasicNameValuePair("barbecue", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/barbecue/delete");
				break;
			}
			case 4: {
				nameValuePairs.add(new BasicNameValuePair("other", message));
				httppost = new HttpPost(
						"http://mtplease.herokuapp.com/pensions/estimate_m/other/delete");
				break;
			}
			}
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
					isDeleteEstimateSuccess = true;
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
				mFragmentCallback.onDeleteEstimateTaskDone(isDeleteEstimateSuccess);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	public void setPeopleNumber(String string) {
		people = Integer.parseInt(string);
		
	}

}
