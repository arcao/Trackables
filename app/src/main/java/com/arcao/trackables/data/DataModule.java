package com.arcao.trackables.data;

import android.content.Context;
import com.arcao.trackables.App;
import com.arcao.trackables.AppConstants;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.io.File;

@Module
public final class DataModule {
	@Singleton
	@Provides
	public Picasso providePicasso(App app, OkHttpDownloader okHttpDownloader) {
		return new Picasso.Builder(app).downloader(okHttpDownloader).indicatorsEnabled(true).build();
	}

	@Singleton
	@Provides
	public OkHttpDownloader provideOkHttpDownloader(OkHttpClient okHttpClient) {
		return new OkHttpDownloader(okHttpClient);
	}

	@Singleton
	@Provides
	public OkHttpClient provideOkHttpClient(Context context) {
		OkHttpClient client = new OkHttpClient();

		File path = new File(context.getCacheDir(), "okHttp");
		path.mkdirs();

		client.setCache(new Cache(path, AppConstants.OKHTTP_DISK_CACHE_SIZE));
		return client;
	}

}
