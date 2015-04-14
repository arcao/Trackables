package com.arcao.trackables.internal.di.component;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.internal.di.ActivityScope;
import com.arcao.trackables.ui.WelcomeActivity;
import com.arcao.trackables.ui.fragment.AfterLoginFragment;
import com.arcao.trackables.ui.fragment.OAuthLoginFragment;
import com.arcao.trackables.ui.fragment.WelcomeFragment;
import com.arcao.trackables.ui.task.AfterLoginTask;
import com.arcao.trackables.ui.task.OAuthLoginTask;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
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
							.build();
		}
		private Initializer() {} // No instances.
	}

}