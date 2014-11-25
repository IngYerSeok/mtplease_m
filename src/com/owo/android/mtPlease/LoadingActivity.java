package com.owo.android.mtPlease;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class LoadingActivity extends Activity implements CalendarDialogFragment.OnDateConfirmedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);

		StartPageFragment startPageFragment = new StartPageFragment();
		changeFragment(startPageFragment);
		
	}
	
	public void changeFragment(Fragment fragment) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.layout_loading_fragments, fragment);

		transaction.commit();
	}

	@Override
	public void onDateConfirmButtonClicked(String dateSelected) {
		// TODO Auto-generated method stub
	}
}
