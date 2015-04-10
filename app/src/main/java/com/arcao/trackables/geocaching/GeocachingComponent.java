package com.arcao.trackables.geocaching;

import android.content.Context;

import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.DeviceInfo;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;

import org.scribe.oauth.OAuthService;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = GeocachingModule.class)
public interface GeocachingComponent {
	GeocachingApi geocachingApi();
	OAuthService oAuthService();
	DeviceInfo deviceInfo();

	final class Initializer {
		public static GeocachingComponent init(Context context) {
			return DaggerGeocachingComponent.builder()
							.appComponent(App.get(context).component())
							.build();
		}
		private Initializer() {} // No instances.
	}
}
