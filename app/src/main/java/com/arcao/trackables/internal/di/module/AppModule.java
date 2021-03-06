package com.arcao.trackables.internal.di.module;

import android.content.Context;

import com.arcao.trackables.App;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
	private final App app;

	public AppModule(App app) {
		this.app = app;
	}

	@Provides
	App provideApp() {
		return app;
	}

	@Provides
	Context provideApplicationContext() {
		return app;
	}
}
