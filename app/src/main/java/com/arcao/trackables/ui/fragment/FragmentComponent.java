package com.arcao.trackables.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import com.arcao.trackables.App;
import com.arcao.trackables.AppComponent;
import com.arcao.trackables.util.di.scope.FragmentScope;
import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class)
public interface FragmentComponent {
	void inject(Fragment fragment);
	void inject(View view);


	final class Initializer {
		public static FragmentComponent init(Context context) {
			AppComponent appComponent = App.get(context.getApplicationContext()).component();
			return Dagger_FragmentComponent.builder()
							.appComponent(appComponent)
							.build();
		}
		private Initializer() {} // No instances.
	}

}
