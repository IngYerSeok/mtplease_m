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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginPageFragment extends Fragment {
	private EditText emailAddress;
	private EditText password;

	ProgressDialog loginProgressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View loginPageView = inflater.inflate(R.layout.fragment_login,
				container, false);

		// 이메일 주소 텍스트에디트
		emailAddress = (EditText) loginPageView
				.findViewById(R.id.editText_emailAddress);
		emailAddress.setText("cto.owo@gmail.com");
		// 비밀번호 텍스트에디트
		password = (EditText) loginPageView
				.findViewById(R.id.editText_password);
		password.setText("1234");

		Button confirmLoginButton = (Button) loginPageView
				.findViewById(R.id.button_login_confirm);
		confirmLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * SearchPageFragment searchPageFragment = new
				 * SearchPageFragment(); FragmentTransaction transaction =
				 * getFragmentManager() .beginTransaction();
				 * transaction.replace(R.id.layout_loading_fragments,
				 * searchPageFragment);
				 * 
				 * transaction.commit();
				 */
				startLoginTask();
			}

		});

		return loginPageView;
	}

	// AsyncTask<Params,Progress,Result>
	private class LoginTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		boolean isLoginSuccess = false;

		public LoginTask(FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpPost httppost = new HttpPost(
					"http://mtplease.herokuapp.com/members/login");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user_id", emailAddress.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("user_password", password.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				JSONObject o = new JSONObject(result.toString());
				Log.i("???why?", o.get("result").toString());
				if (o.get("result").toString() == "true") {
					isLoginSuccess = true;
				} else {
					isLoginSuccess = false;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				mFragmentCallback.onTaskDone(isLoginSuccess, emailAddress.getText().toString());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
	
	public interface FragmentCallback {
		public void onTaskDone(boolean isLoginSuccess, String emailAddress);
	}
	
	private void startLoginTask() {
	      loginProgressDialog = new ProgressDialog(getActivity());
	      loginProgressDialog.setMessage("로그인 중입니다...");
	      loginProgressDialog.setTitle("엠티를부탁해");
	      loginProgressDialog.setCancelable(false);
	      loginProgressDialog.show();

	      LoginTask loginTask = new LoginTask(new FragmentCallback() {

	         @Override
	         public void onTaskDone(boolean isLoginSuccess, String emailAddress) {
	            loginProgressDialog.dismiss();
	            Log.i("emailAddress", emailAddress);

	            if (isLoginSuccess) {
	               Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
	               intent.putExtra("SESSION_ID", emailAddress);
	               startActivity(intent);
	               getActivity().finish();
	            }
	            else {
	               alertDialogBuilder = new AlertDialog.Builder(getActivity());
	               alertDialogBuilder
	                     .setMessage("아이디 또는 비밀번호가 없거나 잘못 입력하셨습니다");
	               alertDialogBuilder.setTitle("로그인 실패");
	               alertDialogBuilder.setCancelable(false);
	               alertDialogBuilder.setPositiveButton("확인",
	                     new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog,
	                              int id) {
	                           alertDialog.dismiss();
	                        }
	                     });

	               alertDialog = alertDialogBuilder.create();
	               alertDialog.show();
	            }
	         }
	      });

	      loginTask.execute();
	   }

}
