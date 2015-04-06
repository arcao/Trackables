package com.arcao.trackables.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.arcao.trackables.App;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

@Module
public final class PreferenceModule {
	@Provides
	SharedPreferences provideSharedPreferences(App app) {
		return PreferenceManager.getDefaultSharedPreferences(app);
	}

	@Provides
	@Named("AccountSharedPreference")
	SharedPreferences provideAccountSharedPreferences(App app) {
		return app.getSharedPreferences("Account", Context.MODE_PRIVATE);
	}
}
