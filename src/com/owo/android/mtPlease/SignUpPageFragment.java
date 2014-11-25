package com.owo.android.mtPlease;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Fragment;
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
import android.widget.Toast;

public class SignUpPageFragment extends Fragment {

	private EditText emailAddress;
	private EditText password;
	private EditText passwordConfirm;
	private EditText phoneNumber;
	private EditText verifiedPhoneNumber;
	private Button verifyPhoneNumberButton;
	private Button signUpButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View signUpPageView = inflater.inflate(R.layout.fragment_signup,
				container, false);

		// 이메일 주소 텍스트에디트
		emailAddress = (EditText) signUpPageView
				.findViewById(R.id.editText_address_email);

		// 비밀번호 텍스트에디트
		password = (EditText) signUpPageView
				.findViewById(R.id.editText_password);

		// 비밀번호 확인 텍스트 에디트
		passwordConfirm = (EditText) signUpPageView
				.findViewById(R.id.editText_password_confirm);
		/*
		// 전화번호 텍스트 에디트
		phoneNumber = (EditText) signUpPageView
				.findViewById(R.id.editText_phoneNum);

		
		// 인증번호
		verifiedPhoneNumber = (EditText) signUpPageView
				.findViewById(R.id.editText_phoneNum_verified);
		
		// 인증번호 전송 버튼
		verifyPhoneNumberButton = (Button) signUpPageView.findViewById(R.id.button_verify_phoneNum);
		verifyPhoneNumberButton.setOnClickListener(new OnClickListener() {
		 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//인증번호 전송 코드
				
			}
			
		});
		*/
		// 회원가입 버튼
		signUpButton = (Button) signUpPageView.findViewById(R.id.button_signup);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SignUpTask().execute();
				
			}
			
		});
		
		

		Log.i("SignUpPageFragment - onCreateView", "called");

		return signUpPageView;
	}

	// AsyncTask<Params,Progress,Result>
	private class SignUpTask extends
			AsyncTask<String, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpPost httppost = new HttpPost("http://mtplease.herokuapp.com/members/join");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000);
			HttpResponse response = null;

			try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", emailAddress.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("user_password", password.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("flag", "1"));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        // Execute HTTP Post Request
		        response = httpclient.execute(httppost);

		        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		        StringBuffer result = new StringBuffer();
		        String line = "";
		        while ((line = rd.readLine()) != null) {
		            result.append(line);
		        }
		        JSONObject o = new JSONObject(result.toString());
		        Log.i("???why?", o.get("result").toString());
		        if(o.get("result").toString() == "true"){
		        	// Toast.makeText(getActivity().getApplicationContext(), "가입성공", Toast.LENGTH_SHORT).show();
		        	
		        	
		        	httppost = new HttpPost("http://mtplease.herokuapp.com/members/login");
		        	httpclient = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000);
					nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("user_id", emailAddress.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("user_password", password.getText().toString()));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        // Execute HTTP Post Request
			        response = httpclient.execute(httppost);
			        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			        result = new StringBuffer();
			        line = "";
			        while ((line = rd.readLine()) != null) {
			            result.append(line);
			        }
			        o = new JSONObject(result.toString());
			        if(o.get("result").toString() == "true"){
//			        	Toast.makeText(getActivity().getApplicationContext(), "로그인성공",
//								Toast.LENGTH_SHORT).show();
			        	Intent intent = new Intent(getActivity()
								.getApplicationContext(), MainActivity.class);
			        	intent.putExtra("emailAddress", emailAddress.getText().toString());
						startActivity(intent);
						getActivity().finish();
			        }
			        else {
//			        	Toast.makeText(getActivity().getApplicationContext(), "로그인실패",
//								Toast.LENGTH_SHORT).show();
			        }
		        	
		        }
		        else {
		        	//Toast.makeText(getActivity().getApplicationContext(), "가입실패", Toast.LENGTH_SHORT).show();
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
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

}
