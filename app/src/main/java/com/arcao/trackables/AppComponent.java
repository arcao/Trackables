package com.arcao.trackables;

import com.arcao.trackables.preference.PreferenceComponent;
import com.arcao.trackables.preference.PreferenceModule;
import com.arcao.trackables.util.di.scope.ApplicationScope;
import dagger.Component;

@ApplicationScope
@Component(
				dependencies = {
								PreferenceComponent.class
				},
				modules = {
								AppModule.class,
								PreferenceModule.class
				}
)
public interface AppComponent extends AppGraph {
	public final static class Initializer {
		public static AppComponent init(App app) {
			return Dagger_AppComponent.builder()
							.appModule(new AppModule(app))
							.preferenceComponent(PreferenceComponent.Initializer.init(app))
							.build();
		}
		private Initializer() {} // No instances.
	}
}
