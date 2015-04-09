package com.arcao.trackables.ui.task;

import android.content.Intent;
import android.os.AsyncTask;
import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.DeviceInfo;
import com.arcao.geocaching.api.data.UserProfile;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.ui.WelcomeActivityComponent;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.ref.WeakReference;

public class OAuthLoginTask extends AsyncTask<String, Void, String[]> {
	public interface TaskListener {
		void onLoginUrlAvailable(String url);
		void onTaskFinished(Intent errorIntent);
	}

	@Inject
	AccountPreferenceHelper accountPreferenceHelper;
	@Inject
	Provider<OAuthService> oAuthServiceProvider;
	@Inject
	Provider<GeocachingApi> geocachingApiProvider;
	@Inject
	Provider<DeviceInfo> deviceInfoProvider;
	@Inject
	ExceptionHandler exceptionHandler;

	private final WeakReference<TaskListener> mTaskListenerRef;
	private Throwable throwable = null;

	public OAuthLoginTask(WelcomeActivityComponent component, TaskListener listener) {
		component.inject(this);
		mTaskListenerRef = new WeakReference<>(listener);
	}


	@Override
	protected String[] doInBackground(String... params) {
		try {
			OAuthService oAuthService = oAuthServiceProvider.get();
			GeocachingApi api = geocachingApiProvider.get();

			if (params.length == 0) {
				Token requestToken = oAuthService.getRequestToken();
				accountPreferenceHelper.setOAuthToken(requestToken);
				String authUrl = oAuthService.getAuthorizationUrl(requestToken);
				Timber.i("AuthorizationUrl: " + authUrl);
				return new String[]{authUrl};
			} else {
				Token requestToken = accountPreferenceHelper.getOAuthToken();
				Token accessToken = oAuthService.getAccessToken(requestToken, new Verifier(params[0]));

				// get account name
				api.openSession(accessToken.getToken());

				UserProfile userProfile = api.getYourUserProfile(false, false, false, false, false, false, deviceInfoProvider.get());

				// add account
				if (accountPreferenceHelper.isAccount()) {
					accountPreferenceHelper.removeAccount();
				}

				accountPreferenceHelper.addAcount(
								userProfile.getUser().getUserName(),
								api.getSession(),
								userProfile.getUser().getAvatarUrl(),
								userProfile.getUser().getMemberType()
				);

				return null;
			}
		} catch (Throwable t) {
			throwable = t;
			return null;
		}
	}

	@Override
	protected void onPostExecute(String[] result) {
		if (throwable != null) {
			handleException(throwable);
			return;
		}

		TaskListener listener = mTaskListenerRef.get();

		if (listener == null)
			return;

		if (result != null && result.length == 1) {
			listener.onLoginUrlAvailable(result[0]);
		}
		else {
			listener.onTaskFinished(null);
		}
	}

	protected void handleException(Throwable t) {
		if (isCancelled())
			return;

		Timber.e(t, t.getMessage());

		Intent intent = exceptionHandler.handle(t);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);

		TaskListener listener = mTaskListenerRef.get();
		if (listener != null) {
			listener.onTaskFinished(intent);
		}
	}
}
