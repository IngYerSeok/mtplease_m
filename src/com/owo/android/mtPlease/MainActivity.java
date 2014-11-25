/**
 * Copyright 2012-2014 Jeremy Feinstein
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.owo.android.mtPlease;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends SlidingActivity implements
		ActionBar.TabListener, CalendarDialogFragment.OnDateConfirmedListener, FragmentCommunicator {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	private final long FINISH_INTERVAL_TIME = 2000;
	long firstBackPressedTime = 0;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Calendar cal = Calendar.getInstance();
	// 선택한 날짜를 저장하기 위한 변수. 초기 값은 현재 날짜이다.
	private String modifiedDate = cal.get(Calendar.YEAR) + "년 "
			+ (cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DATE) + "일";

	/**
	 * UI component들
	 */
	Button searchDrawerCloseButton;
	Spinner locationSelectSpinner;
	Button datePickerButton;
	ImageButton searchButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);

		// setting for the sidedrawer
		// set the content view
		setBehindContentView(R.layout.drawer_search);
		getSlidingMenu().setFadeEnabled(true);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setFadeDegree(0.90f);

		searchDrawerCloseButton = (Button) findViewById(R.id.button_search_drawer_close);
		searchDrawerCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSlidingMenu().showContent();
			}

		});

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// 화면 스와이핑을 할 때마다 로딩되는 좌우 페이지 갯수 설정
		mViewPager.setOffscreenPageLimit(3);

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
/*		Intent i = getIntent();
		Bundle dataBundle = i.getExtras();
		String emailAddress = dataBundle.getString("emailAddress");*/
		//respond(emailAddress, 3);
		

		locationSelectSpinner = (Spinner) findViewById(R.id.spinner_location);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.location_array, R.layout.spinner_text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSelectSpinner.setAdapter(adapter);

		datePickerButton = (Button) findViewById(R.id.button_datepicker);
		datePickerButton.setText(modifiedDate);
		datePickerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager dialogManager = getFragmentManager();
				CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
				calendarDialogFragment
						.setParentCaller(CalendarDialogFragment.PARENT_IS_ACTIVITY);
				calendarDialogFragment.show(dialogManager,
						"calendar_dialog_popped");
			}

		});

		// 검색 버튼 설정 부분
		searchButton = (ImageButton)findViewById(R.id.imageButton_search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String region = locationSelectSpinner.getSelectedItem().toString();
				if(region.equals("대성리로")){
					region = "1";
				}
				else if(region.equals("청평으로")){
					region = "2";
				}
				else {
					region = "3";
				}
				
				String people = ((EditText) findViewById(R.id.editText_numberPeople)).getText()
						.toString();
				
				String[] tmp = (datePickerButton.getText().toString()).split(" ");
				String date = tmp[0].substring(0, 4) + "-" + tmp[1].substring(0, 2) + "-" + tmp[2].substring(0, 2);
				
				String query = "?region="+region+"&people="+people+"&date="+date+"&flag=1";
				sendInfoToFragment(query, 0);
				
				getSlidingMenu().showContent();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		getSlidingMenu().toggle();

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			getSlidingMenu().toggle();
			return true;
		case R.id.action_settings:
			openSetting();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private MainFragment mMainFragment;
        private MyPageFragment mMyPageFragment;
        private CompareFragment mCompareFragment;
        private EstimateFragment mEstimateFragment;
        
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.mMainFragment = (MainFragment) PlaceholderFragment.newInstance(0 + 1);
            this.mCompareFragment = (CompareFragment) PlaceholderFragment.newInstance(1 + 1);
            this.mEstimateFragment = (EstimateFragment) PlaceholderFragment.newInstance(2 + 1);
            this.mMyPageFragment = (MyPageFragment) PlaceholderFragment.newInstance(3 + 1);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Log.d("getItem called", "position : " + position);
			switch (position) {
			case 0:
				return mMainFragment;
			case 1:
				return mCompareFragment;
			case 2:
				return mEstimateFragment;
			case 3:
				return mMyPageFragment;
			}
			Log.e("getItem Error", "return null");
			return null;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		
		public static Fragment newInstance(int sectionNumber) {
			Fragment selectedFragment = null;
			Log.i("MainActivity - newInstance", "" + sectionNumber);

			switch (sectionNumber) {
			case 1:
				selectedFragment = new MainFragment();
				break;
			case 2:
				selectedFragment = new CompareFragment();
				break;
			case 3:
				selectedFragment = new EstimateFragment();
				break;
			case 4:
				selectedFragment = new MyPageFragment();
				break;
			}

			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			selectedFragment.setArguments(args);

			return selectedFragment;
		}

		public PlaceholderFragment() {
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long tempTime = System.currentTimeMillis();
		long intervalTime = tempTime - firstBackPressedTime;

		if (intervalTime >= 0 && FINISH_INTERVAL_TIME >= intervalTime) {
			finish();
		} else {
			firstBackPressedTime = tempTime;
			Toast.makeText(getApplicationContext(), "  '뒤로'버튼을 한 번 더 누르시면 종료됩니다.  ",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void openSetting() {
		SettingsFragment settingFragment = new SettingsFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(android.R.id.content, settingFragment);

		transaction.commit();
	}

	@Override
	public void onDateConfirmButtonClicked(String dateSelected) {
		// TODO Auto-generated method stub
		datePickerButton.setText(dateSelected);
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
			
			String region = locationSelectSpinner.getSelectedItem().toString();
			if(region.equals("대성리로")){
				region = "1";
			}
			else if(region.equals("청평으로")){
				region = "2";
			}
			else {
				region = "3";
			}
			
			String people = ((EditText) findViewById(R.id.editText_numberPeople)).getText()
					.toString();
			
			String[] tmp = (datePickerButton.getText().toString()).split(" ");
			String date = tmp[0].substring(0, 4) + "-" + tmp[1].substring(0, 2) + "-" + tmp[2].substring(0, 2);
			
			String query = "?region="+region+"&people="+people+"&date="+date+"&flag=1";
			sendInfoToFragment(query, 0);
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// nameValuePairs.add(new BasicNameValuePair("region",
			// locationSelectSpinner.getContext().toString()));
			// nameValuePairs.add(new BasicNameValuePair("user_password",
			// password.getText().toString()));
			// httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// // Execute HTTP Post Request
			// response = httpclient.execute(httpget);
			//
			// BufferedReader rd = new BufferedReader(new InputStreamReader(
			// response.getEntity().getContent()));
			// StringBuffer result = new StringBuffer();
			// String line = "";
			// while ((line = rd.readLine()) != null) {
			// result.append(line);
			// }
			// JSONObject o = new JSONObject(result.toString());
			// Log.i("???why?", o.get("result").toString());
			// if (o.get("result").toString() == "true") {
			//
			// // Toast.makeText(getActivity().getApplicationContext(),
			// // "로그인성공",
			// // Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(getActivity()
			// .getApplicationContext(), MainActivity.class);
			// startActivity(intent);
			// getActivity().finish();
			//
			// // Toast.makeText(getActivity().getApplicationContext(),
			// // "로그인실패",
			// // Toast.LENGTH_SHORT).show();
			//
			// } else {
			// // Toast.makeText(getActivity().getApplicationContext(),
			// // "가입실패", Toast.LENGTH_SHORT).show();
			// }
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

	@Override
	public void sendInfoToFragment(String string, int position){
		switch (position) {
		case 0:
			MainFragment mainfrag = (MainFragment) mSectionsPagerAdapter.getItem(position);
			mainfrag.refreshPage(string);
			break;
		case 1:
			CompareFragment comparefrag = (CompareFragment) mSectionsPagerAdapter.getItem(position);
			break;
		case 2:
			EstimateFragment estimatefrag = (EstimateFragment) mSectionsPagerAdapter.getItem(position);
			break;
		case 3:
			MyPageFragment mypagefrag = (MyPageFragment) mSectionsPagerAdapter.getItem(position);
			// mypagefrag.loadMyPage(string);
			break;
		}
	}
}
