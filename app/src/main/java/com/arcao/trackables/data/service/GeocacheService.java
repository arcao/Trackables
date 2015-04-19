package com.arcao.trackables.data.service;

import com.arcao.geocaching.api.data.Geocache;
import com.arcao.trackables.data.api.GeocacheApiService;
import com.arcao.trackables.data.persistence.GeocachePersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class GeocacheService {
	private final GeocacheApiService apiService;
	private final GeocachePersistenceService persistenceService;

	@Inject
	public GeocacheService(GeocacheApiService apiService, GeocachePersistenceService persistenceService) {
		this.apiService = apiService;
		this.persistenceService = persistenceService;
	}

	public Observable<Geocache> getGeocache(String geocacheCode) {
		return apiService.getGeocache(geocacheCode)
						.flatMap(geocache -> persistenceService.putGeocache(geocacheCode, geocache))
						.startWith(persistenceService.getGeocache(geocacheCode));

	}
}