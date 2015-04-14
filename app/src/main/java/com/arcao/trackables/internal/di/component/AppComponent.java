package com.arcao.trackables.internal.di.component;

import com.arcao.trackables.App;
import com.arcao.trackables.data.DataModule;
import com.arcao.trackables.exception.ExceptionModule;
import com.arcao.trackables.geocaching.GeocachingModule;
import com.arcao.trackables.internal.di.AppGraph;
import com.arcao.trackables.internal.di.module.AppModule;
import com.arcao.trackables.preference.PreferenceModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
				DataModule.class,
				AppModule.class,
				ExceptionModule.class,
				GeocachingModule.class,
				PreferenceModule.class
})
public interface AppComponent extends AppGraph {
	void inject(App app);
	App app();

	final class Initializer {
		public static AppComponent init(App app) {
			return DaggerAppComponent.builder()
							.appModule(new AppModule(app))
							.build();
		}
		private Initializer() {} // No instances.
	}
}
