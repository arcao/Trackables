package com.arcao.trackables.preference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arcao.geocaching.api.data.type.MemberType;
import org.scribe.model.Token;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AccountPreferenceHelper {
	private static final String USER_NAME = "USER_NAME";
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String MEMBER_TYPE = "MEMBER_TYPE";
	private static final String AVATAR_URL = "AVATAR_URL";

	private static final String OAUTH_TOKEN_FORMAT = "OAUTH_TOKEN_%s";

	private SharedPreferences mSharedPreferences;

	@Inject
	public AccountPreferenceHelper(@Named("AccountSharedPreference") @NonNull SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
	}

	public boolean isAccount() {
		return getMemberType() != MemberType.Guest;
	}

	public void addAcount(@NonNull String userName, @NonNull String accessToken, @Nullable String avatarUrl, @NonNull MemberType memberType) {
		mSharedPreferences.edit()
						.putString(USER_NAME, userName)
						.putString(ACCESS_TOKEN, accessToken)
						.putString(AVATAR_URL, avatarUrl)
						.putInt(MEMBER_TYPE, memberType.getId())
						.apply();
	}

	@Nullable
	public String getUserName() {
		return mSharedPreferences.getString(USER_NAME, null);
	}

	@Nullable
	public String getAvatarUrl() {
		return mSharedPreferences.getString(AVATAR_URL, null);
	}

	@Nullable
	public String getAccessToken() {
		return mSharedPreferences.getString(ACCESS_TOKEN, null);
	}

	public void removeAccount() {
		mSharedPreferences.edit()
						.remove(USER_NAME)
						.remove(ACCESS_TOKEN)
						.remove(MEMBER_TYPE)
						.remove(AVATAR_URL)
						.apply();
	}

	@NonNull
	public MemberType getMemberType() {
		if (getUserName() == null || getAccessToken() == null)
			return MemberType.Guest;

		return MemberType.getById(mSharedPreferences.getInt(MEMBER_TYPE, MemberType.Guest.getId()));
	}

	public void setOAuthToken(@NonNull Token token) {
		mSharedPreferences.edit()
						.putString(String.format(OAUTH_TOKEN_FORMAT, 0), token.getToken())
						.putString(String.format(OAUTH_TOKEN_FORMAT, 1), token.getSecret())
						.putString(String.format(OAUTH_TOKEN_FORMAT, 2), token.getRawResponse())
						.apply();
	}

	@NonNull
	public Token getOAuthToken() {
		String token = mSharedPreferences.getString(String.format(OAUTH_TOKEN_FORMAT, 0), "");
		String secret = mSharedPreferences.getString(String.format(OAUTH_TOKEN_FORMAT, 1), "");
		String rawResponse = mSharedPreferences.getString(String.format(OAUTH_TOKEN_FORMAT, 2), "");

		return new Token(token, secret, rawResponse);
	}

}
