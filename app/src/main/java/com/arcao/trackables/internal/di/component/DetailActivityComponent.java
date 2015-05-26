package com.arcao.trackables.internal.di.component;


import android.app.Activity;
import com.arcao.trackables.App;
import com.arcao.trackables.internal.di.PerActivity;
import com.arcao.trackables.internal.di.module.ActivityModule;
import com.arcao.trackables.ui.DetailActivity;
import com.arcao.trackables.ui.fragment.detail.AbstractTrackableFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableDetailFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableLogsFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableMapFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableStatisticsFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface DetailActivityComponent {
	// activity
	void inject(DetailActivity activity);

	// fragments
	void inject(AbstractTrackableFragment fragment);
	void inject(TrackableDetailFragment fragment);
	void inject(TrackableLogsFragment fragment);
	void inject(TrackableMapFragment fragment);
	void inject(TrackableStatisticsFragment fragment);

	final class Initializer {
		public static DetailActivityComponent init(Activity activity) {
			return DaggerDetailActivityComponent.builder()
							.activityModule(new ActivityModule(activity))
							.appComponent(App.get(activity).component())
							.build();
		}
		private Initializer() {} // No instances.
	}
}
