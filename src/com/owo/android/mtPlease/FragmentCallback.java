package com.owo.android.mtPlease;

public interface FragmentCallback {
	public void onTaskDone(boolean isLoginSuccess, String emailAddress);
	public void onAddCompareTaskDone(boolean isAddCompareSuccess);
	public void onAddEstimateTaskDone(boolean isAddEstimateSuccess);
	public void onDeleteCompareTaskDone(boolean isDeleteCompareSuccess);
	public void onDeleteEstimateTaskDone(boolean isDeleteEstimateSuccess);
}