package com.arcao.trackables.preference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceHelper {
	private final SharedPreferences mSharedPreferences;
	private String mDeviceId;

	@Inject
	public PreferenceHelper(@NonNull SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
	}

	public String getDeviceId() {
		if (mDeviceId == null) {
			mDeviceId = mSharedPreferences.getString("device_id", null);
		}

		if (mDeviceId == null) {
			mDeviceId = UUID.randomUUID().toString();
			mSharedPreferences.edit().putString("device_id", mDeviceId).apply();
		}

		return mDeviceId;
	}
}
