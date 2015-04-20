package com.arcao.trackables.exception;

import android.content.Context;
import android.content.Intent;
import com.arcao.geocaching.api.exception.InvalidCredentialsException;
import com.arcao.geocaching.api.exception.InvalidResponseException;
import com.arcao.geocaching.api.exception.NetworkException;
import com.arcao.geocaching.api.impl.live_geocaching_api.exception.LiveGeocachingApiException;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.ui.ErrorActivity;
import com.arcao.trackables.ui.WelcomeActivity;
import org.scribe.exceptions.OAuthConnectionException;
import timber.log.Timber;

public class ExceptionHandler {
	protected final Context mContext;
	protected final AccountService accountService;

	public ExceptionHandler(Context ctx, AccountService accountService) {
		mContext = ctx;
		this.accountService = accountService;
	}

	public Intent handle(Throwable t) {
		Timber.e(t, t.getMessage());

		// special handling for some API exceptions
		if (t instanceof LiveGeocachingApiException) {
			Intent intent = handleLiveGeocachingApiExceptions((LiveGeocachingApiException) t);
			if (intent != null)
				return intent;
		}

		ErrorActivity.IntentBuilder builder = new ErrorActivity.IntentBuilder(mContext);

		if (t instanceof InvalidCredentialsException) {
			// TODO remove account
			Intent intent = new Intent(mContext, WelcomeActivity.class);
			return builder.withText(R.string.error_credentials).withIntent(intent).build();
		} else if (t instanceof InvalidResponseException) {
			return builder.withText(R.string.error_invalid_response).withAdditionalMessage(t.getMessage()).build();
		} else if (t instanceof NetworkException || t instanceof OAuthConnectionException) {
			return builder.withText(R.string.error_network).build();
		} else {
			String message = t.getMessage();
			if (message == null)
				message = "";

			return builder.withAdditionalMessage(String.format("%s\n\nException: %s", message, t.getClass().getSimpleName())).build();
		}
	}

	protected Intent handleLiveGeocachingApiExceptions(LiveGeocachingApiException t) {
		switch (t.getStatusCode()) {
			case NotAuthorized:
				accountService.logout();
				return null;
			case CacheLimitExceeded: // 118: user reach the quota limit
			case NumberOfCallsExceeded: // 140: too many method calls per minute

			default:
				return null;
		}
	}
}
