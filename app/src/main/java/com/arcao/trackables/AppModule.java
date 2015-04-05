package com.arcao.trackables;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
	private final App app;

	public AppModule(App app) {
		this.app = app;
	}

	@Provides
	Application provideApplication() {
		return app;
	}
}
