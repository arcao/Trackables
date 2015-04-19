package com.arcao.trackables.data.persistence;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableTravel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TrackablePersistenceService {
	private final ClassPersister mainPersister;
	private final ClassPersister trackablePersister;
	private final ClassPersister trackableTravelListPersister;

	@Inject
	public TrackablePersistenceService(
					@Named(PersistenceModule.PERSISTANCE_MAIN) ClassPersister mainPersister,
					@Named(PersistenceModule.PERSISTANCE_TRACKABLE) ClassPersister trackablePersister,
					@Named(PersistenceModule.PERSISTANCE_TRACKABLE_TRAVEL) ClassPersister trackableTravelListPersister) {
		this.mainPersister = mainPersister;
		this.trackablePersister = trackablePersister;
		this.trackableTravelListPersister = trackableTravelListPersister;
	}

	public Observable<List<Trackable>> getUserTrackables() {
		return getTrackables("userTrackables");
	}

	public Observable<List<Trackable>> putUserTrackables(List<Trackable> userTrackableList) {
		return putTrackables("userTrackables", userTrackableList);
	}

	public Observable<List<Trackable>> getTrackables(String category) {
		return mainPersister.getList(category, Trackable.class);
	}

	public Observable<List<Trackable>> putTrackables(String category, List<Trackable> trackableList) {
		return mainPersister.putList(category, trackableList, Trackable.class);
	}

	public Observable<Trackable> getTrackable(String trackingCode) {
		return trackablePersister.get(trackingCode, Trackable.class);
	}

	public Observable<Trackable> putTrackable(String trackingCode, Trackable trackable) {
		return trackablePersister.put(trackingCode, trackable);
	}

	public Observable<List<TrackableTravel>> getTrackableTravelList(String trackingCode) {
		return trackableTravelListPersister.getList(trackingCode, TrackableTravel.class);
	}

	public Observable<List<TrackableTravel>> putTrackableTravelList(String trackingCode, List<TrackableTravel> trackableTravelList) {
		return trackableTravelListPersister.putList(trackingCode, trackableTravelList, TrackableTravel.class);
	}

}
