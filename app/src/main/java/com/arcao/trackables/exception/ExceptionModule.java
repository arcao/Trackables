package com.arcao.trackables.exception;

import com.arcao.trackables.App;
import com.arcao.trackables.data.service.AccountService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public final class ExceptionModule {
	@Singleton
	@Provides
	public ExceptionHandler provideExceptionHandler(App app, AccountService accountService) {
		return new ExceptionHandler(app, accountService);
	}
}
