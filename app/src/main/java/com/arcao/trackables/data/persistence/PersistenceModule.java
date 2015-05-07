package com.arcao.trackables.data.persistence;

import android.content.Context;

import com.arcao.trackables.data.persistence.jackson.mixin.MixinModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import au.com.gridstone.grex.converter.Converter;
import au.com.gridstone.grex.converters.JacksonConverter;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class PersistenceModule {
	public static final String PERSISTANCE_MAIN = "base";
	public static final String PERSISTANCE_TRACKABLE = "trackable";
	public static final String PERSISTANCE_TRACKABLE_TRAVEL = "trackableTravel";
	public static final String PERSISTANCE_GEOCACHE = "geocache";

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_MAIN)
	ClassPersister provideMainPersister(Context context, Converter converter) {
		File directory = context.getDir(PERSISTANCE_MAIN, Context.MODE_PRIVATE);
		return new ClassPersister(converter, directory);
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_TRACKABLE)
	ClassPersister provideTrackablePersister(Context context, Converter converter) {
		return new ClassPersister(converter, getCacheDir(context, PERSISTANCE_TRACKABLE));
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_TRACKABLE_TRAVEL)
	ClassPersister provideTrackableTravelPersister(Context context, Converter converter) {
		return new ClassPersister(converter, getCacheDir(context, PERSISTANCE_TRACKABLE_TRAVEL));
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_GEOCACHE)
	ClassPersister provideGeocachePersister(Context context, Converter converter) {
		return new ClassPersister(converter, getCacheDir(context, PERSISTANCE_GEOCACHE));
	}

	@Provides
	@Singleton
	Converter provideConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new MixinModule());

		return new JacksonConverter(objectMapper);
	}

	private File getCacheDir(Context context, String dirName) {
		File directory = new File(context.getCacheDir(), dirName);
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Timber.e("Unable to create " + directory);
			}
		}

		return directory;
	}

}
