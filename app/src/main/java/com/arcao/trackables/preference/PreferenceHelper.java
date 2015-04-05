package com.arcao.trackables.preference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceHelper {
	private SharedPreferences mSharedPreferences;

	@Inject
	public PreferenceHelper(@NonNull SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
	}


}
