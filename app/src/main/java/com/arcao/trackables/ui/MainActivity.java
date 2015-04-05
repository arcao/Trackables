package com.arcao.trackables.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.preference.PreferenceHelper;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

import javax.inject.Inject;

public class MainActivity extends MaterialNavigationDrawer<Fragment> {
	@Inject
	AccountPreferenceHelper accountPreferenceHelper;

	@Inject
	PreferenceHelper preferenceHelper;

	@Override
	public void init(Bundle savedInstanceState) {
		ActivityComponent.Initializer.init(this).inject(this);


	}

	@Override
	public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onPostCreate(savedInstanceState, persistentState);

		if (!accountPreferenceHelper.isAccount()) {
			startActivity(new Intent(this, LogInActivity.class));
			finish();
			return;
		}
	}
}
