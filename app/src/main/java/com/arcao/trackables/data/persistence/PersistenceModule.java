package com.arcao.trackables.data.persistence;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import au.com.gridstone.grex.converter.Converter;
import au.com.gridstone.grex.converters.JacksonConverter;
import dagger.Module;
import dagger.Provides;

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
		return new ClassPersister(context, PERSISTANCE_MAIN, converter);
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_TRACKABLE)
	ClassPersister provideTrackablePersister(Context context, Converter converter) {
		return new ClassPersister(context, PERSISTANCE_TRACKABLE, converter);
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_TRACKABLE_TRAVEL)
	ClassPersister provideTrackableTravelPersister(Context context, Converter converter) {
		return new ClassPersister(context, PERSISTANCE_TRACKABLE_TRAVEL, converter);
	}

	@Provides
	@Singleton
	@Named(PersistenceModule.PERSISTANCE_GEOCACHE)
	ClassPersister provideGeocachePersister(Context context, Converter converter) {
		return new ClassPersister(context, PERSISTANCE_GEOCACHE, converter);
	}

	@Provides
	@Singleton
	Converter provideConverter() {
		return new JacksonConverter();
	}

}
