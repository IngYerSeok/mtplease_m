package com.owo.android.mtPlease;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarDialogFragment extends DialogFragment {
	
	public static final int PARENT_IS_FRAGMENT = 0;
	public static final int PARENT_IS_ACTIVITY = 1;
	private int parentCaller = -1;

	String modifiedDate = null;

	OnDateConfirmedListener mCallback;

	/**
	 * 
	 * @author In-Ho 액티비티에 날짜 선택 다이얼로그에서 선택된 날짜 값을 string 타입으로 전달해주는 interface
	 */
	public interface OnDateConfirmedListener {
		public void onDateConfirmButtonClicked(String dateSelected);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnDateConfirmedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDateConfirmedSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View calendarDialogView = inflater.inflate(R.layout.dialog_calendar,
				container, false);

		getDialog().setTitle("엠티 출발 날짜를 선택하세요");

		CalendarView cal = (CalendarView) calendarDialogView
				.findViewById(R.id.calendarView);
		cal.setShowWeekNumber(false);

		// 달력 날짜 출력 범위 설정
		long mindate = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
		long maxdate = System.currentTimeMillis() + 31536000000L;
		cal.setMinDate(mindate);
		cal.setMaxDate(maxdate);

		cal.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				// TODO Auto-generated method stub
				modifiedDate = year + "년 " + (month + 1) + "월 " + dayOfMonth
						+ "일";
			}
		});

		Button dateConfirmButton = (Button) calendarDialogView
				.findViewById(R.id.button_date_confirm);
		dateConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// SearchPageFragment로 변경된 날짜값을 보내준다
				switch(parentCaller) {
					case PARENT_IS_FRAGMENT:
						if (modifiedDate != null) {
							Intent intent = new Intent();
							intent.putExtra("date_chosen", modifiedDate);
							getTargetFragment().onActivityResult(
									getTargetRequestCode(), 1111, intent);
							dismiss();
						} else {
							dismiss();
						}
						break;
					case PARENT_IS_ACTIVITY:
						if (modifiedDate != null) {
							mCallback.onDateConfirmButtonClicked(modifiedDate);
							dismiss();
						} else {
							dismiss();
						}
						break;
					default:
						Log.e("CalendarDialogFragment", "parentCaller is not set");
						break;
				}
			}

		});

		return calendarDialogView;
	}
	
	/**
	 * @param parentCaller
	 * CalendarDialogFragment가 Fragment에서 불렸으면 PARENT_IS_FRAGMENT으로,
	 * Activity에서 불렸으면 PARENT_IS_ACTIVITY로 parameter를 설정.
	 */
	void setParentCaller(int parentCaller) {
		this.parentCaller = parentCaller;
	}
}
