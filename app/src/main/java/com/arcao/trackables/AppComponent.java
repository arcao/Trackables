package com.arcao.trackables;

import com.arcao.trackables.exception.ExceptionModule;
import com.arcao.trackables.geocaching.GeocachingModule;
import com.arcao.trackables.preference.PreferenceModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
				modules = {
								AppModule.class,
								ExceptionModule.class,
								GeocachingModule.class,
								PreferenceModule.class
				}
)
public interface AppComponent extends AppGraph {
	final class Initializer {
		public static AppComponent init(App app) {
			return Dagger_AppComponent.builder()
							.appModule(new AppModule(app))
							.build();
		}
		private Initializer() {} // No instances.
	}
}
