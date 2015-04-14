package com.arcao.trackables.internal.di.component;

import android.app.Activity;
import com.arcao.trackables.App;
import com.arcao.trackables.internal.di.PerActivity;
import com.arcao.trackables.internal.di.module.ActivityModule;
import com.arcao.trackables.ui.WelcomeActivity;
import com.arcao.trackables.ui.fragment.AfterLoginFragment;
import com.arcao.trackables.ui.fragment.OAuthLoginFragment;
import com.arcao.trackables.ui.fragment.WelcomeFragment;
import com.arcao.trackables.ui.task.AfterLoginTask;
import com.arcao.trackables.ui.task.OAuthLoginTask;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
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
		public static WelcomeActivityComponent init(Activity activity) {
			return DaggerWelcomeActivityComponent.builder()
							.activityModule(new ActivityModule(activity))
							.appComponent(App.get(activity).component())
							.build();
		}
		private Initializer() {} // No instances.
	}

}
