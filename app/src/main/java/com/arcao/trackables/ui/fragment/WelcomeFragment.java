package com.arcao.trackables.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arcao.trackables.R;
import com.arcao.trackables.ui.WelcomeActivity;
import com.arcao.trackables.ui.WelcomeActivityComponent;

public class WelcomeFragment extends Fragment {
	public static WelcomeFragment newInstance() {
		return new WelcomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WelcomeActivityComponent component = ((WelcomeActivity)getActivity()).component();
		component.inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_welcome, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@OnClick(R.id.buttonSign)
	public void switchToOAuthLogin() {
		WelcomeActivity activity = (WelcomeActivity) getActivity();
		if (activity != null)
			activity.switchTo(WelcomeActivity.WelcomeState.LOGIN);
	}
}
