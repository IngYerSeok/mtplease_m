package com.owo.android.mtPlease;

public interface FragmentCallback {
	public void onTaskDone(boolean isLoginSuccess, String emailAddress);
	public void onAddCompareTaskDone(boolean isAddCompareSuccess);
	public void onAddEstimateTaskDone(boolean isAddEstimateSuccess);
}