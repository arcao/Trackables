package com.arcao.trackables.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.WelcomeActivityComponent;
import com.arcao.trackables.ui.fragment.AfterLoginFragment;
import com.arcao.trackables.ui.fragment.OAuthLoginFragment;
import com.arcao.trackables.ui.fragment.WelcomeFragment;
import timber.log.Timber;

import javax.inject.Inject;

public class WelcomeActivity extends AppCompatActivity implements HasComponent<WelcomeActivityComponent> {
	public enum WelcomeState {
		WELCOME,
		LOGIN,
		AFTER_LOGIN,
		FINISHED
	}

	@Inject
	protected AccountService accountService;

	private WelcomeActivityComponent component;
	public WelcomeActivityComponent component() {
		if (component == null)
			component = WelcomeActivityComponent.Initializer.init(this);
		return component;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		component().inject(this);

		setContentView(R.layout.activity_welcome);

		if (getFragmentManager().findFragmentById(R.id.fragment_container) == null && savedInstanceState == null) {
			switchTo(WelcomeState.WELCOME);
		}
	}

	public void showError(Intent errorIntent) {
		accountService.logout();
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
				startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
				finish();
				return;
		}

		Timber.d("Switching to %s", fragment.getClass().getName());
		getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}
}
