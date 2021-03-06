package com.arcao.trackables.geocaching;

import android.os.Build;
import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.configuration.impl.DefaultStagingGeocachingApiConfiguration;
import com.arcao.geocaching.api.data.DeviceInfo;
import com.arcao.geocaching.api.impl.LiveGeocachingApi;
import com.arcao.trackables.App;
import com.arcao.trackables.AppConstants;
import com.arcao.trackables.BuildConfig;
import com.arcao.trackables.geocaching.oauth.GeocachingOAuthProvider;
import dagger.Module;
import dagger.Provides;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

import javax.inject.Singleton;

@Module
public final class GeocachingModule {
	@Singleton
	@Provides
	public GeocachingApi provideGeocachingApi() {
		if (BuildConfig.GEOCACHING_API_STAGING) {
			return LiveGeocachingApi.Builder.liveGeocachingApi().withConfiguration(new DefaultStagingGeocachingApiConfiguration()).build();
		} else {
			return LiveGeocachingApi.Builder.liveGeocachingApi().build();
		}
	}

	@Singleton
	@Provides
	public OAuthService provideOAuthService() {
		ServiceBuilder serviceBuilder = new ServiceBuilder()
						.apiKey(BuildConfig.GEOCACHING_API_KEY)
						.apiSecret(BuildConfig.GEOCACHING_API_SECRET)
						.callback(AppConstants.OAUTH_CALLBACK_URL)
						.debug();

		if (BuildConfig.GEOCACHING_API_STAGING) {
			serviceBuilder.provider(GeocachingOAuthProvider.Staging.class);
		} else {
			serviceBuilder.provider(GeocachingOAuthProvider.class);
		}

		return serviceBuilder.build();
	}

	@Singleton
	@Provides
	public DeviceInfo provideDeviceInfo(App app) {
		return new DeviceInfo(
						0,
						0,
						app.getVersion(),
						Build.MANUFACTURER,
						Build.MODEL,
						Build.VERSION.RELEASE,
						0,
						app.getDeviceId(),
						null,
						null
		);

	}

}
