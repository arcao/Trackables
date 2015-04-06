package com.arcao.trackables;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.arcao.trackables.preference.PreferenceHelper;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import timber.log.Timber;

import javax.inject.Inject;

public class App extends Application {
	private AppComponent component;

	@Inject
	PreferenceHelper preferenceHelper;

	@Override
	public void onCreate() {
		super.onCreate();


		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			// TODO Crashlytics.start(this);
			// TODO Timber.plant(new CrashlyticsTree());
		}

		buildComponentAndInject();

		//initialize and create the image loader logic
		DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
			@Override
			public void set(ImageView imageView, Uri uri, Drawable placeholder) {
				Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
			}

			@Override
			public void cancel(ImageView imageView) {
				Picasso.with(imageView.getContext()).cancelRequest(imageView);
			}
		});
	}

	public void buildComponentAndInject() {
		component = AppComponent.Initializer.init(this);
		component.inject(this);
	}
	public AppComponent component() {
		return component;
	}
	public static App get(Context context) {
		return (App) context.getApplicationContext();
	}

	public String getVersion() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Timber.e(e, e.getMessage());
			return "1.0";
		}

	}

	public String getDeviceId() {
		return preferenceHelper.getDeviceId();
	}
}
