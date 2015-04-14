package com.arcao.trackables.internal.di;

import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.DeviceInfo;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.preference.PreferenceHelper;
import com.squareup.picasso.Picasso;
import org.scribe.oauth.OAuthService;

/**
 * Created by msloup on 10.4.2015.
 */
public interface AppGraph {
	// from DataModule
	Picasso picasso();

	// from ExceptionModule
	ExceptionHandler exceptionHandler();

	// from GeocachingModule
	GeocachingApi geocachingApi();
	OAuthService oAuthService();
	DeviceInfo deviceInfo();

	// from PreferenceModule
	PreferenceHelper preferenceHelper();
	AccountPreferenceHelper accountPreferenceHelper();
}