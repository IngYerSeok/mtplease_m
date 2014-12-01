package com.owo.android.mtPlease;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartPageFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View startPageView = inflater.inflate(R.layout.fragment_startpage, container,
				false);
		
		Button facebookLoginButton = (Button) startPageView.findViewById(R.id.button_loading_facebook_login);
		facebookLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginPageFragment loginPageFragment = new LoginPageFragment();
				loadPageFragment(loginPageFragment, "LOGIN_PAGE_FRAGMENT");
			}
        	
        });
		
		Button signupButton = (Button) startPageView.findViewById(R.id.button_loading_sign_up);
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SignUpPageFragment signUpPageFragment = new SignUpPageFragment();
				loadPageFragment(signUpPageFragment, "SIGNUP_PAGE_FRAGMENT");
			}
        	
        });
		
		return startPageView;
	}
	
	public void loadPageFragment(Fragment fragment, String fragmentTag) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.layout_loading_fragments, fragment, fragmentTag).addToBackStack(fragmentTag);

		transaction.commit();
	}
}
