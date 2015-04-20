package com.arcao.trackables.data.service;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.DeviceInfo;
import com.arcao.geocaching.api.data.UserProfile;
import com.arcao.geocaching.api.data.type.MemberType;
import com.arcao.geocaching.api.exception.GeocachingApiException;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

@Singleton
public class AccountService {
	private static final String PREF__USER_NAME = "USER_NAME";
	private static final String PREF__ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String PREF__MEMBER_TYPE = "MEMBER_TYPE";
	private static final String PREF__AVATAR_URL = "AVATAR_URL";
	private static final String PREF__OAUTH_TOKEN_FORMAT = "OAUTH_TOKEN_%s";

	private final SharedPreferences mSharedPreferences;
	private final GeocachingApi geocachingApi;
	private final Provider<OAuthService> oAuthServiceProvider;
	private final Provider<DeviceInfo> deviceInfoProvider;

	@Inject
	public AccountService(@Named("AccountSharedPreference") SharedPreferences mSharedPreferences,
												GeocachingApi geocachingApi, Provider<OAuthService> oAuthServiceProvider,
												Provider<DeviceInfo> deviceInfoProvider) {
		this.mSharedPreferences = mSharedPreferences;
		this.geocachingApi = geocachingApi;
		this.oAuthServiceProvider = oAuthServiceProvider;
		this.deviceInfoProvider = deviceInfoProvider;

		login();
	}

	public void login() {
		try {
			if (isAccount())
				geocachingApi.openSession(getAccessToken());
		} catch (GeocachingApiException e) {
			Timber.e(e.getMessage(), e);
		}
	}

	public void logout() {
		geocachingApi.closeSession();

		mSharedPreferences.edit()
						.remove(PREF__USER_NAME)
						.remove(PREF__ACCESS_TOKEN)
						.remove(PREF__MEMBER_TYPE)
						.remove(PREF__AVATAR_URL)
						.apply();
	}

	public boolean isAccount() {
		return getMemberType() != MemberType.Guest;
	}

	@Nullable
	public String getUserName() {
		return mSharedPreferences.getString(PREF__USER_NAME, null);
	}

	@Nullable
	public String getAvatarUrl() {
		return mSharedPreferences.getString(PREF__AVATAR_URL, null);
	}

	@NonNull
	public MemberType getMemberType() {
		if (getUserName() == null || getAccessToken() == null)
			return MemberType.Guest;

		return MemberType.getById(mSharedPreferences.getInt(PREF__MEMBER_TYPE, MemberType.Guest.getId()));
	}

	public Observable<String> startOAuthLogin() {
		return Observable.create(subscriber -> {
			OAuthService oAuthService = oAuthServiceProvider.get();

			Token requestToken = oAuthService.getRequestToken();
			storeOAuthRequestToken(requestToken);

			String authUrl = oAuthService.getAuthorizationUrl(requestToken);

			Timber.i("AuthorizationUrl: " + authUrl);
			subscriber.onNext(authUrl);
			subscriber.onCompleted();
		});
	}

	public Observable<Void> finishOAuthLogin(String verifier) {
		return Observable.create(subscriber -> {
			try {
				OAuthService oAuthService = oAuthServiceProvider.get();

				Token requestToken = loadOAuthRequestToken();
				Token accessToken = oAuthService.getAccessToken(requestToken, new Verifier(verifier));

				// get account name
				geocachingApi.openSession(accessToken.getToken());

				UserProfile userProfile = geocachingApi.getYourUserProfile(false, false, false, false, false, false, deviceInfoProvider.get());

				// add account
				if (isAccount()) {
					logout();
				}

				addAccount(
								userProfile.getUser().getUserName(),
								geocachingApi.getSession(),
								userProfile.getUser().getAvatarUrl(),
								userProfile.getUser().getMemberType()
				);

				removeOAuthRequestToken();
				login();

				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		});
	}

	// ---------- helper methods -------------
	private void addAccount(@NonNull String userName, @NonNull String accessToken, @Nullable String avatarUrl, @NonNull MemberType memberType) {
		mSharedPreferences.edit()
						.putString(PREF__USER_NAME, userName)
						.putString(PREF__ACCESS_TOKEN, accessToken)
						.putString(PREF__AVATAR_URL, avatarUrl)
						.putInt(PREF__MEMBER_TYPE, memberType.getId())
						.apply();
	}

	private void storeOAuthRequestToken(@NonNull Token token) {
		mSharedPreferences.edit()
						.putString(String.format(PREF__OAUTH_TOKEN_FORMAT, 0), token.getToken())
						.putString(String.format(PREF__OAUTH_TOKEN_FORMAT, 1), token.getSecret())
						.putString(String.format(PREF__OAUTH_TOKEN_FORMAT, 2), token.getRawResponse())
						.apply();
	}

	private void removeOAuthRequestToken() {
		mSharedPreferences.edit()
						.remove(String.format(PREF__OAUTH_TOKEN_FORMAT, 0))
						.remove(String.format(PREF__OAUTH_TOKEN_FORMAT, 1))
						.remove(String.format(PREF__OAUTH_TOKEN_FORMAT, 2))
						.apply();
	}

	@NonNull
	private Token loadOAuthRequestToken() {
		String token = mSharedPreferences.getString(String.format(PREF__OAUTH_TOKEN_FORMAT, 0), "");
		String secret = mSharedPreferences.getString(String.format(PREF__OAUTH_TOKEN_FORMAT, 1), "");
		String rawResponse = mSharedPreferences.getString(String.format(PREF__OAUTH_TOKEN_FORMAT, 2), "");

		return new Token(token, secret, rawResponse);
	}

	@Nullable
	private String getAccessToken() {
		return mSharedPreferences.getString(PREF__ACCESS_TOKEN, null);
	}
}
