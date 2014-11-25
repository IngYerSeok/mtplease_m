package com.owo.android.mtPlease;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

/**
 * @author In-Ho 어플 실행 및 로그인 후에 뜨는 검색창. 사이드 드로어에 있는 검색창과는 다르게 Frgament로 구현
 */
public class SearchPageFragment extends Fragment {

	View searchPageView;

	final Fragment this_fragment = this;

	private Calendar cal = Calendar.getInstance();
	// 선택한 날짜를 저장하기 위한 변수. 초기 값은 현재 날짜이다.
	private String modifiedDate = cal.get(Calendar.YEAR) + "년 "
			+ cal.get(Calendar.MONTH) + "월 " + cal.get(Calendar.DATE) + "일";

	/**
	 * UI component들
	 */
	Button searchFragmentCloseButton;
	Spinner locationSelectSpinner;
	Button datePickerButton;
	ImageButton searchButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		searchPageView = inflater.inflate(R.layout.drawer_search, container,
				false);

		searchFragmentCloseButton = (Button) searchPageView
				.findViewById(R.id.button_search_drawer_close);
		searchFragmentCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}

		});

		// 장소 선정하는 드롭다운 메뉴 설정 부분
		locationSelectSpinner = (Spinner) searchPageView
				.findViewById(R.id.spinner_location);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.location_array, R.layout.spinner_text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSelectSpinner.setAdapter(adapter);

		datePickerButton = (Button) searchPageView
				.findViewById(R.id.button_datepicker);
		datePickerButton.setText(modifiedDate);
		datePickerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager dialogManager = getFragmentManager();
				CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
				calendarDialogFragment
						.setParentCaller(CalendarDialogFragment.PARENT_IS_FRAGMENT);
				calendarDialogFragment.setTargetFragment(this_fragment, 1111);
				calendarDialogFragment.show(dialogManager,
						"calendar_dialog_popped");
			}

		});

		// 검색 버튼 설정 부분
		searchButton = (ImageButton) searchPageView
				.findViewById(R.id.imageButton_search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 서버 데이터 요청 코드
				 * */
				new SearchTask().execute();
//				Intent intent = new Intent(getActivity()
//						.getApplicationContext(), MainActivity.class);
//				startActivity(intent);
//				getActivity().finish();
			}

		});

		Log.i("SearchPageFragment - onCreateView", "called");

		return searchPageView;
	}

	/**
	 * @author In-Ho CalendarDialogFragment에서 오는 콜백 메소드
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1111) { // make sure fragment codes match up
			modifiedDate = data.getStringExtra("date_chosen");
			datePickerButton.setText(modifiedDate);
		}
	}
	
	// AsyncTask<Params,Progress,Result>
	private class SearchTask extends AsyncTask<String, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(String... urls) {
			
			HttpGet httpget = new HttpGet(
					"http://mtplease.herokuapp.com/pensions/search_m");
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					10000);
			HttpResponse response = null;
			Log.i("region", locationSelectSpinner.getContext().toString());
			Log.i("people", ((Button) searchPageView
					.findViewById(R.id.editText_numberPeople)).getText().toString());
			Log.i("date", datePickerButton.getText().toString());
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);

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
