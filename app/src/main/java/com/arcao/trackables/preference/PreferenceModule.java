package com.arcao.trackables.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

@Module
public final class PreferenceModule {
	private final Context mApplicationContext;

	public PreferenceModule(Context applicationContext) {
		mApplicationContext = applicationContext;
	}

	@Provides
	SharedPreferences provideSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(mApplicationContext);
	}

	@Provides
	@Named("AccountSharedPreference")
	SharedPreferences provideAccountSharedPreferences() {
		return mApplicationContext.getSharedPreferences("Account", Context.MODE_PRIVATE);
	}
}
