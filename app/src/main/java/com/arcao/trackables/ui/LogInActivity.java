package com.arcao.trackables.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.preference.PreferenceHelper;

import javax.inject.Inject;

public class LogInActivity extends ActionBarActivity {
	@Inject
	AccountPreferenceHelper accountPreferenceHelper;

	@Inject
	PreferenceHelper preferenceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityComponent.Initializer.init(this).inject(this);
	}
}
