package com.arcao.trackables.ui;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.preference.PreferenceComponent;
import com.arcao.trackables.util.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = {
				AppComponent.class,
				PreferenceComponent.class
})
public interface MainActivityComponent {
	// activity
	void inject(MainActivity activity);

	final class Initializer {
		public static MainActivityComponent init(Context context) {
			return DaggerMainActivityComponent.builder()
							.appComponent(App.get(context).component())
							.preferenceComponent(PreferenceComponent.Initializer.init(context))
							.build();
		}
		private Initializer() {} // No instances.
	}
}
