package com.arcao.trackables;

import com.arcao.trackables.exception.ExceptionModule;
import com.arcao.trackables.geocaching.GeocachingModule;
import com.arcao.trackables.preference.PreferenceModule;
import dagger.Component;

@Component(modules = {
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
