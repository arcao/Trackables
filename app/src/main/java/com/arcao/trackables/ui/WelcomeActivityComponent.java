package com.arcao.trackables.ui;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.exception.ExceptionComponent;
import com.arcao.trackables.geocaching.GeocachingComponent;
import com.arcao.trackables.preference.PreferenceComponent;
import com.arcao.trackables.ui.fragment.AfterLoginFragment;
import com.arcao.trackables.ui.fragment.OAuthLoginFragment;
import com.arcao.trackables.ui.fragment.WelcomeFragment;
import com.arcao.trackables.ui.task.AfterLoginTask;
import com.arcao.trackables.ui.task.OAuthLoginTask;
import com.arcao.trackables.util.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = {
				AppComponent.class,
				PreferenceComponent.class,
				GeocachingComponent.class,
				ExceptionComponent.class
})
public interface WelcomeActivityComponent {
	// activity
	void inject(WelcomeActivity activity);

	// fragments
	void inject(AfterLoginFragment fragment);
	void inject(OAuthLoginFragment fragment);
	void inject(WelcomeFragment fragment);

	// tasks
	void inject(AfterLoginTask task);
	void inject(OAuthLoginTask task);

	final class Initializer {
		public static WelcomeActivityComponent init(Context context) {
			return DaggerWelcomeActivityComponent.builder()
							.appComponent(App.get(context).component())
							.preferenceComponent(PreferenceComponent.Initializer.init(context))
							.geocachingComponent(GeocachingComponent.Initializer.init(context))
							.exceptionComponent(ExceptionComponent.Initializer.init(context))
							.build();
		}
		private Initializer() {} // No instances.
	}

}
