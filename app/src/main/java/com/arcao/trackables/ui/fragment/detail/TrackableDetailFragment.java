package com.arcao.trackables.ui.fragment.detail;

import android.os.Bundle;
import android.view.View;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.DataSource;
import com.arcao.trackables.data.service.TrackableService;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.internal.rx.AndroidSchedulerTransformer;

import javax.inject.Inject;

import rx.android.app.AppObservable;
import rx.internal.util.SubscriptionList;

public class TrackableDetailFragment extends AbstractTrackableFragment {
	@Inject
	protected TrackableService trackableService;
	@Inject
	protected ExceptionHandler exceptionHandler;

	private final SubscriptionList subscriptionList = new SubscriptionList();

	public static TrackableDetailFragment newInstance(String trackableCode) {
		TrackableDetailFragment fragment = new TrackableDetailFragment();
		fragment.setTrackableCode(trackableCode);
		return fragment;
	}

	@Override
	protected int getLayout() {
		return R.layout.fragment_trackable_detail;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		component().inject(this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		AppObservable.bindFragment(this, trackableService.getTrackable(getTrackableCode(), DataSource.REMOTE_IF_NECESSARY))
						.compose(AndroidSchedulerTransformer.get())
						.subscribe(this::showTrackableDetail, throwable -> {
							startActivity(exceptionHandler.handle(throwable));
							getActivity().finish();
						});
	}

	public void showTrackableDetail(Trackable trackable) {

	}
}
