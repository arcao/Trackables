package com.arcao.trackables;

import android.app.Application;
import android.content.Context;
import timber.log.Timber;

public class App extends Application {
	private AppComponent component;

	@Override
	public void onCreate() {
		super.onCreate();


		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			// TODO Crashlytics.start(this);
			// TODO Timber.plant(new CrashlyticsTree());
		}

		buildComponentAndInject();
	}

	public void buildComponentAndInject() {
		component = AppComponent.Initializer.init(this);
		component.inject(this);
	}
	public AppComponent component() {
		return component;
	}
	public static App get(Context context) {
		return (App) context.getApplicationContext();
	}
}
