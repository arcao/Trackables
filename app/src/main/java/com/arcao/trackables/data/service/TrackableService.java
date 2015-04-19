package com.arcao.trackables.data.service;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableTravel;
import com.arcao.trackables.data.api.TrackableApiService;
import com.arcao.trackables.data.persistence.TrackablePersistenceService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TrackableService {
	private final TrackableApiService apiService;
	private final TrackablePersistenceService persistenceService;

	@Inject
	public TrackableService(TrackableApiService apiService, TrackablePersistenceService persistenceService) {
		this.apiService = apiService;
		this.persistenceService = persistenceService;
	}

	public Observable<List<Trackable>> getUserTrackables() {
		return apiService.getUserTrackables()
						.flatMap(persistenceService::putUserTrackables)
						.startWith(persistenceService.getUserTrackables());
	}

	public Observable<Trackable> getTrackable(String trackableCode) {
		return apiService.getTrackable(trackableCode)
						.flatMap(trackable -> persistenceService.putTrackable(trackableCode, trackable))
						.startWith(persistenceService.getTrackable(trackableCode));
	}

	public Observable<List<TrackableTravel>> getTrackableTravel(String trackableCode) {
		return apiService.getTrackableTravelList(trackableCode)
						.flatMap(trackableTravelList -> persistenceService.putTrackableTravelList(trackableCode, trackableTravelList))
						.startWith(persistenceService.getTrackableTravelList(trackableCode));
	}
}