package com.arcao.trackables.ui.task;

import android.content.Intent;
import android.os.AsyncTask;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.geocaching.api.data.TrackableTravel;
import com.arcao.geocaching.api.util.GeocachingUtils;
import com.arcao.trackables.data.service.GeocacheService;
import com.arcao.trackables.data.service.TrackableService;
import com.arcao.trackables.exception.ExceptionHandler;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

public class AfterLoginTask extends AsyncTask<Void, AfterLoginTask.TaskListener.ProgressState, Void> {
	public  interface TaskListener {
		void onProgressStateChanged(ProgressState state);
		void onTaskFinished(Intent intent);

		enum ProgressState {
			RETRIEVE_TRACKABLES,
			RETRIEVE_TRACKABLE_TRAVELS,
			RETRIEVE_GEOCACHES
		}
	}

	@Inject
	protected ExceptionHandler exceptionHandler;

	@Inject
	protected TrackableService trackableService;

	@Inject
	protected GeocacheService geocacheService;

	private final WeakReference<TaskListener> mTaskListenerRef;
	private Throwable throwable = null;

	public AfterLoginTask(TaskListener listener) {
		mTaskListenerRef = new WeakReference<>(listener);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			publishProgress(TaskListener.ProgressState.RETRIEVE_TRACKABLES);

			List<Trackable> trackables = trackableService.getUserTrackables().toBlocking().single();

			publishProgress(TaskListener.ProgressState.RETRIEVE_TRACKABLE_TRAVELS);

			Set<String> geocaches = new HashSet<>();
			for(Trackable trackable : trackables) {
				List<TrackableTravel> trackableTravels = trackableService.getTrackableTravel(trackable.getTrackingNumber()).toBlocking().single();

				for(TrackableTravel trackableTravel : trackableTravels) {
					if (trackableTravel.getCacheID() > 0)
						geocaches.add(GeocachingUtils.cacheIdToCacheCode(trackableTravel.getCacheID()));
				}
			}

			publishProgress(TaskListener.ProgressState.RETRIEVE_GEOCACHES);
			Observable.from(geocaches).flatMap(geocacheService::getGeocache).toBlocking();
		} catch (Throwable e) {
			throwable = e;

			if (throwable instanceof RuntimeException && throwable.getCause() != null)
				throwable = throwable.getCause();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (throwable != null) {
			handleException(throwable);
			return;
		}

		TaskListener listener = mTaskListenerRef.get();
		if (listener != null) {
			listener.onTaskFinished(null);
		}
	}

	@Override
	protected void onProgressUpdate(TaskListener.ProgressState... progress) {
		if (progress.length != 1)
			return;

		TaskListener listener = mTaskListenerRef.get();
		if (listener != null) {
			listener.onProgressStateChanged(progress[0]);
		}
	}

	protected void handleException(Throwable t) {
		if (isCancelled())
			return;

		Timber.e(t, t.getMessage());

		Intent intent = exceptionHandler.handle(t);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);

		TaskListener listener = mTaskListenerRef.get();
		if (listener != null) {
			listener.onTaskFinished(intent);
		}
	}

}
