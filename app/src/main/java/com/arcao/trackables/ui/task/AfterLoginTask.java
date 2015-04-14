package com.arcao.trackables.ui.task;

import android.content.Intent;
import android.os.AsyncTask;
import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.ref.WeakReference;

public class AfterLoginTask extends AsyncTask<Void, AfterLoginTask.TaskListener.ProgressState, Void> {
	public  interface TaskListener {
		void onProgressStateChanged(ProgressState state);
		void onTaskFinished(Intent intent);

		enum ProgressState {
			RETRIEVE_TRACKABLES
		}
	}

	@Inject
	AccountPreferenceHelper accountPreferenceHelper;
	@Inject
	Provider<GeocachingApi> geocachingApiProvider;
	@Inject
	ExceptionHandler exceptionHandler;

	private final WeakReference<TaskListener> mTaskListenerRef;
	private Throwable throwable = null;

	public AfterLoginTask(TaskListener listener) {
		mTaskListenerRef = new WeakReference<>(listener);
	}

	@Override
	protected Void doInBackground(Void... params) {
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
