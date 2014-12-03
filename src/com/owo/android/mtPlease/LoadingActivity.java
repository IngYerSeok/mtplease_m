package com.owo.android.mtPlease;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class LoadingActivity extends Activity implements
		CalendarDialogFragment.OnDateConfirmedListener {

	private final long FINISH_INTERVAL_TIME = 2000;
	private long firstBackPressedTime = 0;

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

		transaction.replace(R.id.layout_loading_fragments, fragment,
				"START_PAGE_FRAGMENT");

		transaction.commit();
	}

	@Override
	public void onDateConfirmButtonClicked(String dateSelected) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long tempTime = System.currentTimeMillis();
		long intervalTime = tempTime - firstBackPressedTime;

		if (getFragmentManager().getBackStackEntryCount() == 0) {
			if (intervalTime >= 0 && FINISH_INTERVAL_TIME >= intervalTime) {
				finish();
			} else {
				firstBackPressedTime = tempTime;
				Toast.makeText(this,
						"'�ڷ�' ��ư�� �� �� �� �����ø� ����˴ϴ�.", Toast.LENGTH_SHORT).show();
			}
		} else {
			getFragmentManager().popBackStack();
		}
	}

	/**
	 * @author In-Ho ����ڰ� edittext�κ� �̿��� ȭ���� ��ġ���� �� Ű���带 ���ְ� ��Ŀ���� �����ϱ� ���� �޼ҵ�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}
}
