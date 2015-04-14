package com.arcao.trackables.internal.di.component;

import android.app.Activity;
import com.arcao.trackables.App;
import com.arcao.trackables.internal.di.PerActivity;
import com.arcao.trackables.internal.di.module.ActivityModule;
import com.arcao.trackables.ui.MainActivity;
import com.arcao.trackables.ui.adapter.TrackableListAdapter;
import com.arcao.trackables.ui.fragment.TrackableListFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface MainActivityComponent {
	// activity
	void inject(MainActivity activity);

	// fragment
	void inject(TrackableListFragment fragment);

	// adapter
	void inject(TrackableListAdapter adapter);
	void inject(TrackableListAdapter.ViewHolder holder);


	final class Initializer {
		public static MainActivityComponent init(Activity activity) {
			return DaggerMainActivityComponent.builder()
							.activityModule(new ActivityModule(activity))
							.appComponent(App.get(activity).component())
							.build();
		}
		private Initializer() {} // No instances.
	}
}
