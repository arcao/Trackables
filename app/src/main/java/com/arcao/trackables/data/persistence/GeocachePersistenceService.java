package com.arcao.trackables.data.persistence;

import com.arcao.geocaching.api.data.Geocache;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class GeocachePersistenceService {
	private final ClassPersister geocachePersister;

	@Inject
	public GeocachePersistenceService(@Named(PersistenceModule.PERSISTENCE_GEOCACHE) ClassPersister geocachePersister) {
		this.geocachePersister = geocachePersister;
	}

	public Observable<Geocache> getGeocache(String geocacheCode) {
		return geocachePersister.get(geocacheCode, Geocache.class);
	}

	public Observable<Geocache> putGeocache(String geocacheCode, Geocache geocache) {
		return geocachePersister.put(geocacheCode, geocache);
	}
}
