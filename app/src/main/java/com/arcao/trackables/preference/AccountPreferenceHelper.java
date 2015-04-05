package com.arcao.trackables.preference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arcao.geocaching.api.data.type.MemberType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AccountPreferenceHelper {
	private SharedPreferences mSharedPreferences;

	@Inject
	public AccountPreferenceHelper(@Named("AccountSharedPreference") @NonNull SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
	}

	public boolean isAccount() {
		return getMemeberType() != MemberType.Guest;
	}

	@Nullable
	public String getUserName() {
		return null;
	}

	@Nullable
	public String getAvatarUrl() {
		return null;
	}

	@NonNull
	public MemberType getMemeberType() {
		return MemberType.Guest;
	}
}
