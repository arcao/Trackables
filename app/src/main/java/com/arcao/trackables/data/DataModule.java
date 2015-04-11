package com.arcao.trackables.data;

import com.arcao.trackables.App;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public final class DataModule {
	@Provides
	public Picasso providePicasso(App app, OkHttpDownloader okHttpDownloader) {
		return new Picasso.Builder(app).downloader(okHttpDownloader).build();
	}

	@Provides
	public OkHttpDownloader provideOkHttpDownloader(OkHttpClient okHttpClient) {
		return new OkHttpDownloader(okHttpClient);
	}

	@Provides
	public OkHttpClient provideOkHttpClient() {
		return new OkHttpClient();
	}

}
