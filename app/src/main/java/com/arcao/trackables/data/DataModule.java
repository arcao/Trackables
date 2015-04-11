package com.arcao.trackables.data;

import com.arcao.trackables.App;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public final class DataModule {
	@Provides
	public Picasso providePicasso(App app) {
		return Picasso.with(app);
	}

}
