package com.arcao.trackables.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.util.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
	void inject(Activity activity);
	void inject(View view);


	final static class Initializer {
		static ActivityComponent init(Context context) {
			AppComponent appComponent = App.get(context.getApplicationContext()).component();
			return Dagger_ActivityComponent.builder()
							.appComponent(appComponent)
							.build();
		}
		private Initializer() {} // No instances.
	}
}
