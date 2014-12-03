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

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.Toast;

public class SignUpPageFragment extends Fragment {

	static final int LOGIN_SUCCESSFUL = 1;
	static final int LOGIN_UNSUCCESSFUL = 0;
	static final int SIGNUP_UNSUCCESSFUL = -1;

	private EditText emailAddress;
	private EditText password;
	private EditText passwordConfirm;
	private EditText phoneNumber;
	private EditText verifiedPhoneNumber;
	private Button verifyPhoneNumberButton;
	private Button signUpButton;

	ProgressDialog loginProgressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

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
		 * // 전화번호 텍스트 에디트 phoneNumber = (EditText) signUpPageView
		 * .findViewById(R.id.editText_phoneNum);
		 * 
		 * 
		 * // 인증번호 verifiedPhoneNumber = (EditText) signUpPageView
		 * .findViewById(R.id.editText_phoneNum_verified);
		 * 
		 * // 인증번호 전송 버튼 verifyPhoneNumberButton = (Button)
		 * signUpPageView.findViewById(R.id.button_verify_phoneNum);
		 * verifyPhoneNumberButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //인증번호 전송 코드
		 * 
		 * }
		 * 
		 * });
		 */
		// 회원가입 버튼
		signUpButton = (Button) signUpPageView.findViewById(R.id.button_signup);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidEmailAddress(emailAddress.getText().toString())) {
					if (isPasswordEqual(password.getText().toString(),
							passwordConfirm.getText().toString())) {
						if (isValidPassword(password.getText().toString())) {
							startSignUpTask();
						} else {
							Log.i("signup", "not valid password");
							Toast.makeText(getActivity(),
									"비밀번호는 최소 6자리 이상이여야 합니다.",
									Toast.LENGTH_SHORT).show();
							// toast 비밀번호는 최소 6자리 이상이여야 합니다.
						}
					} else {
						Log.i("signup", "not password equal");
						// toast 비밀번호가 일치하지 않습니다.
						Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Log.i("signup", "not valid emailAddress");
					// toast 유효하지 않은 이메일 형식입니다.
					Toast.makeText(getActivity(), "유효하지 않은 이메일 형식입니다.",
							Toast.LENGTH_SHORT).show();
				}
			}

			private boolean isValidPassword(String password) {
				if (password.length() >= 6) {
					return true;
				}
				return false;
			}

			private boolean isPasswordEqual(String password,
					String passwordConfirm) {
				if (password.compareTo(passwordConfirm) == 0) {
					return true;
				} else {
					return false;
				}
			}

			private boolean isValidEmailAddress(String email) {
				String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
				java.util.regex.Pattern p = java.util.regex.Pattern
						.compile(ePattern);
				java.util.regex.Matcher m = p.matcher(email);
				return m.matches();
			}

		});

		Log.i("SignUpPageFragment - onCreateView", "called");

		return signUpPageView;
	}

	private void startSignUpTask() {
		loginProgressDialog = new ProgressDialog(getActivity());
		loginProgressDialog.setMessage("회원가입 중 입니다...");
		loginProgressDialog.setTitle("엠티를부탁해");
		loginProgressDialog.setCancelable(false);
		loginProgressDialog.show();

		alertDialogBuilder = new AlertDialog.Builder(getActivity());

		LoginTask loginTask = new LoginTask(new FragmentCallback() {

			@Override
			public void onTaskDone(int flagSignUpSuccess) {
				loginProgressDialog.dismiss();

				switch (flagSignUpSuccess) {
				case LOGIN_SUCCESSFUL:
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), MainActivity.class);
					intent.putExtra("SESSION_ID", emailAddress.getText().toString());
					startActivity(intent);
					getActivity().finish();
					break;
				case LOGIN_UNSUCCESSFUL:
					alertDialogBuilder
							.setMessage("서버와의 통신이 원할하지 않습니다. 다시 로그인해주세요.");
					alertDialogBuilder.setTitle("로그인 실패");
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialog.dismiss();
									Intent intent = new Intent(getActivity()
											.getApplicationContext(),
											LoadingActivity.class);
									startActivity(intent);
									getActivity().finish();
								}
							});

					alertDialog = alertDialogBuilder.create();
					alertDialog.show();
					break;
				case SIGNUP_UNSUCCESSFUL:
				default:
					alertDialogBuilder.setMessage("동일한 ID가 존재합니다. 다시 가입해주세요.");
					alertDialogBuilder.setTitle("가입 실패");
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									alertDialog.dismiss();
									Intent intent = new Intent(getActivity()
											.getApplicationContext(),
											LoadingActivity.class);
									startActivity(intent);
									getActivity().finish();
								}
							});

					alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			}
		});

		loginTask.execute();
	}

	public interface FragmentCallback {
		public void onTaskDone(int flagSignUpSuccess);
	}

	// AsyncTask<Params,Progress,Result>
	private class LoginTask extends AsyncTask<String, Void, HttpResponse> {
		private FragmentCallback mFragmentCallback;
		int signUpSuccess = SIGNUP_UNSUCCESSFUL;

		public LoginTask(FragmentCallback fragmentCallback) {
			mFragmentCallback = fragmentCallback;
		}

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpPost httppost = new HttpPost(
					"http://mtplease.herokuapp.com/members/join");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("user_id",
						emailAddress.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("user_password",
						password.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("flag", "1"));
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
					// Toast.makeText(getActivity().getApplicationContext(),
					// "가입성공", Toast.LENGTH_SHORT).show();

					httppost = new HttpPost(
							"http://mtplease.herokuapp.com/members/login");
					httpclient = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(
							httpclient.getParams(), 10000);
					nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("user_id",
							emailAddress.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("user_password",
							password.getText().toString()));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					response = httpclient.execute(httppost);
					rd = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent()));
					result = new StringBuffer();
					line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					o = new JSONObject(result.toString());
					if (o.get("result").toString() == "true") {
						signUpSuccess = LOGIN_SUCCESSFUL;
					} else {
						signUpSuccess = LOGIN_UNSUCCESSFUL;
					}
				} else {
					signUpSuccess = SIGNUP_UNSUCCESSFUL;
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
				mFragmentCallback.onTaskDone(signUpSuccess);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
}
