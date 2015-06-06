package com.arcao.trackables.ui.fragment.detail;

import android.os.Bundle;
import android.view.View;

import com.arcao.trackables.R;

public class TrackableStatisticsFragment extends AbstractTrackableFragment {
	public static TrackableStatisticsFragment newInstance(String trackableCode) {
		TrackableStatisticsFragment fragment = new TrackableStatisticsFragment();
		fragment.setTrackableCode(trackableCode);
		return fragment;
	}

	@Override
	protected int getLayout() {
		return R.layout.fragment_trackable_statistics;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}