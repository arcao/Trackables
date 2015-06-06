package com.arcao.trackables.ui.fragment.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcao.geocaching.api.data.Trackable;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.DataSource;
import com.arcao.trackables.data.service.TrackableService;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.internal.rx.AndroidSchedulerTransformer;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.app.AppObservable;

public class TrackableDetailFragment extends AbstractTrackableFragment {
	@Inject
	protected TrackableService trackableService;
	@Inject
	protected ExceptionHandler exceptionHandler;
	@Inject
	protected Picasso picasso;

	@InjectView(R.id.image)
	protected ImageView imageView;

	@InjectView(R.id.title)
	protected TextView titleView;

	@InjectView(R.id.tracking_code)
	protected TextView trackingCodeView;

	@InjectView(R.id.description)
	protected TextView descriptionView;

	@InjectView(R.id.goal)
	protected TextView goalView;

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

		ButterKnife.inject(this, view);

		AppObservable.bindFragment(this, trackableService.getTrackable(getTrackableCode(), DataSource.REMOTE_IF_NECESSARY))
						.compose(AndroidSchedulerTransformer.get())
						.subscribe(this::showTrackableDetail, throwable -> {
							startActivity(exceptionHandler.handle(throwable));
							getActivity().finish();
						});
	}

	public void showTrackableDetail(Trackable trackable) {
		getActivity().setTitle(trackable.getName());

		titleView.setText(trackable.getName());
		trackingCodeView.setText(trackable.getTrackingNumber());
		descriptionView.setText(trackable.getDescription());
		goalView.setText(trackable.getGoal());

		if (trackable.getImages().size() > 0) {
			picasso.load(trackable.getImages().get(0).getUrl()).into(imageView);
		} else {
			imageView.setVisibility(View.GONE);
		}
	}
}
