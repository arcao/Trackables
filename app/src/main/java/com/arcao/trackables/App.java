package com.arcao.trackables;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.arcao.trackables.internal.di.component.AppComponent;
import com.arcao.trackables.preference.PreferenceHelper;
import com.arcao.trackables.ui.util.picasso.CropSquareTransformation;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import timber.log.Timber;

import javax.inject.Inject;

public class App extends Application {
	private AppComponent component;

	@Inject
	PreferenceHelper preferenceHelper;

	@Inject
	Picasso picasso;

	@Override
	public void onCreate() {
		super.onCreate();


		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		} else {
			// TODO Crashlytics.start(this);
			// TODO Timber.plant(new CrashlyticsTree());
		}

		component().inject(this);

		//initialize and create the image loader logic
		DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
			@Override
			public void set(ImageView imageView, Uri uri, Drawable placeholder) {
				picasso.load(uri).placeholder(placeholder).transform(new CropSquareTransformation()).into(imageView);
			}

			@Override
			public void cancel(ImageView imageView) {
				picasso.cancelRequest(imageView);
			}
		});
	}

	public AppComponent component() {
		if (component == null) {
			component = AppComponent.Initializer.init(this);
		}
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
