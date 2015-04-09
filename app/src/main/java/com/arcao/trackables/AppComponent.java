package com.arcao.trackables;

import com.arcao.trackables.preference.PreferenceModule;
import dagger.Component;

@Component(modules = {
				AppModule.class,
				PreferenceModule.class
})
public interface AppComponent {
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
