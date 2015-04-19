package com.arcao.trackables.data.api;

import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableTravel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TrackableApiService {
	private static final int TRACKABLE_PER_REQUEST = 30;

	private final GeocachingApi geocachingApi;

	@Inject
	public TrackableApiService(GeocachingApi geocachingApi) {
		this.geocachingApi = geocachingApi;
	}

	public Observable<List<Trackable>> getUserTrackables() {
		return Observable.create(subscriber -> {
			try {
				List<Trackable> list = new ArrayList<>();
				List<Trackable> retrieved = null;

				do {
					retrieved = geocachingApi.getUsersTrackables(list.size(), TRACKABLE_PER_REQUEST, 0, false);
				} while (retrieved != null && list.addAll(retrieved));

				subscriber.onNext(list);
				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		});
	}

	public Observable<Trackable> getTrackable(String trackableCode) {
		return Observable.create(subscriber -> {
			try {
				subscriber.onNext(geocachingApi.getTrackable(trackableCode, 0));
				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		});
	}

	public Observable<List<TrackableTravel>> getTrackableTravelList(String trackableCode) {
		return Observable.create(subscriber -> {
			try {
				subscriber.onNext(geocachingApi.getTrackableTravelList(trackableCode));
				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		});
	}
}
