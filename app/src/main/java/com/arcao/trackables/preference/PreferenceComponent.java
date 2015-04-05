package com.arcao.trackables.preference;

import android.content.Context;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules=PreferenceModule.class)
public interface PreferenceComponent {
	PreferenceHelper preferenceHelper();
	AccountPreferenceHelper accountPreferenceHelper();

	final class Initializer {
		public static PreferenceComponent init(Context applicationContext) {
			return Dagger_PreferenceComponent.builder()
							.preferenceModule(new PreferenceModule(applicationContext))
							.build();
		}
		private Initializer() {} // No instances.
	}
}
