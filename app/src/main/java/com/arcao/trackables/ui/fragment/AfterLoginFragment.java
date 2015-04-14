package com.arcao.trackables.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.arcao.trackables.R;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.ui.WelcomeActivity;
import com.arcao.trackables.internal.di.component.WelcomeActivityComponent;
import com.arcao.trackables.ui.task.AfterLoginTask;

public class AfterLoginFragment extends Fragment implements AfterLoginTask.TaskListener, HasComponent<WelcomeActivityComponent> {
	private static final String STATE__PROGRESS_STATE = "STATE__PROGRESS_STATE";

	@InjectView(R.id.progressMessage)
	TextView mProgressMessage;

	private AfterLoginTask mTask;
	private ProgressState lastProgressState;

	public static Fragment newInstance() {
		return new AfterLoginFragment();
	}

	public WelcomeActivityComponent component() {
		return ((WelcomeActivity)getActivity()).component();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		component().inject(this);

		mTask = new AfterLoginTask(this);
		component().inject(mTask);
		mTask.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mTask != null)
			mTask.cancel(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE__PROGRESS_STATE, lastProgressState.ordinal());
		super.onSaveInstanceState(outState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_after_login, container, false);
		ButterKnife.inject(this, view);

		ProgressState state = ProgressState.RETRIEVE_TRACKABLES;

		if (savedInstanceState != null) {
			state = ProgressState.values()[savedInstanceState.getInt(STATE__PROGRESS_STATE, state.ordinal())];
		}

		onProgressStateChanged(state);
		return view;
	}

	@Override
	public void onProgressStateChanged(ProgressState state) {
		lastProgressState = state;

		int resMessage;
		switch (state) {
			default:
			case RETRIEVE_TRACKABLES:
				resMessage = R.string.login_progress_retrieve_trackables;
		}

		mProgressMessage.setText(resMessage);
	}

	@Override
	public void onTaskFinished(Intent errorIntent) {
		WelcomeActivity activity = (WelcomeActivity) getActivity();
		if (activity == null)
			return;

		if (errorIntent == null) {
			activity.switchTo(WelcomeActivity.WelcomeState.FINISHED);
		}
		else {
			activity.showError(errorIntent);
		}
	}
}
