package com.arcao.trackables.internal.di.module;

import android.app.Activity;
import com.arcao.trackables.internal.di.PerActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
	private final Activity activity;

	public ActivityModule(Activity activity) {
		this.activity = activity;
	}

	@PerActivity
	@Provides
	public Activity provideActivity() {
		return activity;
	}
}
