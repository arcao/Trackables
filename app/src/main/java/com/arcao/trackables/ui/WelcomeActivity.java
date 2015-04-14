package com.arcao.trackables.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.arcao.trackables.R;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.WelcomeActivityComponent;
import com.arcao.trackables.ui.fragment.AfterLoginFragment;
import com.arcao.trackables.ui.fragment.OAuthLoginFragment;
import com.arcao.trackables.ui.fragment.WelcomeFragment;
import timber.log.Timber;

public class WelcomeActivity extends ActionBarActivity implements HasComponent<WelcomeActivityComponent> {
	public enum WelcomeState {
		WELCOME,
		LOGIN,
		AFTER_LOGIN,
		FINISHED
	}

	private WelcomeActivityComponent component;
	public WelcomeActivityComponent component() {
		if (component == null)
			component = WelcomeActivityComponent.Initializer.init(this);
		return component;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);

		if (getFragmentManager().findFragmentById(R.id.fragment_container) == null && savedInstanceState == null) {
			switchTo(WelcomeState.WELCOME);
		}
	}

	public void showError(Intent errorIntent) {
		switchTo(WelcomeState.WELCOME);
		startActivity(errorIntent);
	}

	public void switchTo(WelcomeState state) {
		Fragment fragment;

		switch (state) {
			default:
			case WELCOME:
				fragment = WelcomeFragment.newInstance();
				break;

			case LOGIN:
				fragment  = OAuthLoginFragment.newInstance();
				break;

			case AFTER_LOGIN:
				fragment = AfterLoginFragment.newInstance();
				break;

			case FINISHED:
				finish();
				return;
		}

		Timber.d("Switching to %s", fragment.getClass().getName());
		getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}
}
