package com.arcao.trackables.exception;

import com.arcao.trackables.App;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public final class ExceptionModule {
	@Singleton
	@Provides
	public ExceptionHandler provideExceptionHandler(App app) {
		return new ExceptionHandler(app);
	}
}
