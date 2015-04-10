package com.arcao.trackables.ui;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.util.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface MainActivityComponent {
	// activity
	void inject(MainActivity activity);

	final class Initializer {
		public static MainActivityComponent init(Context context) {
			return DaggerMainActivityComponent.builder()
							.appComponent(App.get(context).component())
							.build();
		}
		private Initializer() {} // No instances.
	}
}
