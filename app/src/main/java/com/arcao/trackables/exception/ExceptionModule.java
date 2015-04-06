package com.arcao.trackables.exception;

import com.arcao.trackables.App;
import dagger.Module;
import dagger.Provides;

@Module
public final class ExceptionModule {
	@Provides
	public ExceptionHandler provideExceptionHandler(App app) {
		return new ExceptionHandler(app);
	}
}
