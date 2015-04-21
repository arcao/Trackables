package com.arcao.trackables.data.service;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableTravel;
import com.arcao.trackables.data.api.TrackableApiService;
import com.arcao.trackables.data.persistence.TrackablePersistenceService;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TrackableService {
	private final TrackableApiService apiService;
	private final TrackablePersistenceService persistenceService;

	@Inject
	public TrackableService(TrackableApiService apiService, TrackablePersistenceService persistenceService) {
		this.apiService = apiService;
		this.persistenceService = persistenceService;
	}

	public Observable<List<Trackable>> getUserTrackables(DataSource source) {
		switch (source) {
			case LOCAL:
				return persistenceService.getUserTrackables();
			case REMOTE:
				return apiService.getUserTrackables()
								.flatMap(persistenceService::putUserTrackables);
			case REFRESH:
				return apiService.getUserTrackables()
								.flatMap(persistenceService::putUserTrackables)
								.startWith(persistenceService.getUserTrackables());
			default:
			case REMOTE_IF_NECESSARY:
				return persistenceService.getUserTrackables()
								.switchIfEmpty(Observable.defer(() -> apiService.getUserTrackables()
												.flatMap(persistenceService::putUserTrackables)));
		}
	}

	public Observable<Trackable> getTrackable(String trackableCode, DataSource source) {
		switch (source) {
			case LOCAL:
				return persistenceService.getTrackable(trackableCode);
			case REMOTE:
				return apiService.getTrackable(trackableCode)
								.flatMap(trackable -> persistenceService.putTrackable(trackableCode, trackable));
			case REFRESH:
				return apiService.getTrackable(trackableCode)
								.flatMap(trackable -> persistenceService.putTrackable(trackableCode, trackable))
								.startWith(persistenceService.getTrackable(trackableCode));
			default:
			case REMOTE_IF_NECESSARY:
				return persistenceService.getTrackable(trackableCode)
								.switchIfEmpty(Observable.defer(() -> apiService.getTrackable(trackableCode)
												.flatMap(trackable -> persistenceService.putTrackable(trackableCode, trackable))));
		}
	}

	public Observable<List<TrackableTravel>> getTrackableTravel(String trackableCode, DataSource source) {
		switch (source) {
			case LOCAL:
				return persistenceService.getTrackableTravelList(trackableCode);
			case REMOTE:
				return apiService.getTrackableTravelList(trackableCode)
								.flatMap(trackableTravelList -> persistenceService.putTrackableTravelList(trackableCode, trackableTravelList));
			case REFRESH:
				return apiService.getTrackableTravelList(trackableCode)
								.flatMap(trackableTravelList -> persistenceService.putTrackableTravelList(trackableCode, trackableTravelList))
								.startWith(persistenceService.getTrackableTravelList(trackableCode));
			default:
			case REMOTE_IF_NECESSARY:
				return persistenceService.getTrackableTravelList(trackableCode)
								.switchIfEmpty(Observable.defer(() -> apiService.getTrackableTravelList(trackableCode)
												.flatMap(trackableTravelList -> persistenceService.putTrackableTravelList(trackableCode, trackableTravelList))));
		}
	}
}