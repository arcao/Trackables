package com.arcao.trackables.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.util.di.Injectable;
import com.arcao.trackables.util.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
	void inject(Activity activity);
	void inject(View view);
	void inject(Fragment fragment);
	void inject(Injectable injectable);


	final static class Initializer {
		public static ActivityComponent init(Context context) {
			AppComponent appComponent = App.get(context.getApplicationContext()).component();
			return Dagger_ActivityComponent.builder()
							.appComponent(appComponent)
							.build();
		}
		private Initializer() {} // No instances.
	}
}
