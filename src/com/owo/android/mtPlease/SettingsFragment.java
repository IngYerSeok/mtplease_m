package com.owo.android.mtPlease;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferencescreen_settings);
	}
}
