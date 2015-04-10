package com.arcao.trackables.preference;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PreferenceModule.class)
public interface PreferenceComponent {
	PreferenceHelper preferenceHelper();
	AccountPreferenceHelper accountPreferenceHelper();

	final class Initializer {
		public static PreferenceComponent init(Context context) {
			return DaggerPreferenceComponent.builder()
							.appComponent(App.get(context).component())
							.build();
		}
		private Initializer() {} // No instances.
	}

}
