package com.arcao.trackables.data.service;

import com.arcao.geocaching.api.data.Geocache;
import com.arcao.trackables.data.api.GeocacheApiService;
import com.arcao.trackables.data.persistence.GeocachePersistenceService;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

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
		return getGeocache(geocacheCode, DataSource.REMOTE_IF_NECESSARY);
	}

	public Observable<Geocache> getGeocache(String geocacheCode, DataSource source) {
		switch (source) {
			case LOCAL:
				return persistenceService.getGeocache(geocacheCode);
			case REMOTE:
				return apiService.getGeocache(geocacheCode)
								.flatMap(geocache -> persistenceService.putGeocache(geocacheCode, geocache));
			case REFRESH:
				return apiService.getGeocache(geocacheCode)
								.flatMap(geocache -> persistenceService.putGeocache(geocacheCode, geocache))
								.startWith(persistenceService.getGeocache(geocacheCode));
			default:
			case REMOTE_IF_NECESSARY:
				return persistenceService.getGeocache(geocacheCode)
								.switchIfEmpty(Observable.defer(() -> apiService.getGeocache(geocacheCode)));
		}
	}
}