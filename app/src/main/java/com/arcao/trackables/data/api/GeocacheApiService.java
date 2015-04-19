package com.arcao.trackables.data.api;

import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.Geocache;
import com.arcao.geocaching.api.impl.live_geocaching_api.filter.CacheCodeFilter;
import com.arcao.trackables.internal.rx.OnSubscribePublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class GeocacheApiService {
	private final GeocachingApi geocachingApi;
	private GeocacheRetrieveThread thread;

	@Inject
	public GeocacheApiService(GeocachingApi geocachingApi) {
		this.geocachingApi = geocachingApi;
	}

	public Observable<Geocache> getGeocache(String geocacheCode) {
		if (thread == null) {
			thread = new GeocacheRetrieveThread(geocachingApi, GeocachingApi.ResultQuality.LITE);
			thread.start();
		}

		return Observable.create(thread.retrieve(geocacheCode));
	}

	private static class GeocacheRetrieveThread extends Thread {
		private static final int GEOCACHE_PER_REQUEST = 10;
		private static final int SINGLE_ITEM_WAIT_MILLIS = 25;

		private final GeocachingApi geocachingApi;
		private final GeocachingApi.ResultQuality resultQuality;
		private final BlockingQueue<GeocacheRequest> requestQueue;

		public GeocacheRetrieveThread(GeocachingApi geocachingApi, GeocachingApi.ResultQuality resultQuality) {
			this.geocachingApi = geocachingApi;
			this.resultQuality = resultQuality;
			requestQueue = new LinkedBlockingQueue<>();
		}

		public synchronized Observable.OnSubscribe<Geocache> retrieve(String geocacheCode) {
			GeocacheRequest request = new GeocacheRequest(geocacheCode);

			requestQueue.add(request);

			return request;
		}

		@Override
		public void run() {
			try {
				while (true) {
					List<GeocacheRequest> requests = new ArrayList<>();

					requests.add(requestQueue.take());

					if (requestQueue.isEmpty()) {
						wait(SINGLE_ITEM_WAIT_MILLIS);
					}

					while (!requestQueue.isEmpty() && requests.size() < GEOCACHE_PER_REQUEST) {
						requests.add(requestQueue.remove());
					}

					try {
						List<Geocache> results = geocachingApi.searchForGeocaches(resultQuality, requests.size(), 0, 0,
										Collections.singletonList(new CacheCodeFilter(geocacheCodesFromRequests(requests))),
										null);

						for (GeocacheRequest request :  requests) {
							request.publish(findGeocacheInList(results, request.geocacheCode));
						}

					} catch (Exception e) {
						for (GeocacheRequest request :  requests) {
							request.publishException(e);
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private String[] geocacheCodesFromRequests(List<GeocacheRequest> requests) {
			String[] result = new String[requests.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = requests.get(i).geocacheCode;
			}

			return result;
		}

		private Geocache findGeocacheInList(Collection<Geocache> list, String cacheCode) {
			for (Geocache geocache : list) {
				if (geocache.getCode().equals(cacheCode))
					return geocache;
			}
			return null;
		}
	}

	private static class GeocacheRequest extends OnSubscribePublisher<Geocache> {
		public final String geocacheCode;

		public GeocacheRequest(String geocacheCode) {
			this.geocacheCode = geocacheCode;
		}
	}
}
