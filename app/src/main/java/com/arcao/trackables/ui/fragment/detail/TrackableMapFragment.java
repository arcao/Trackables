package com.arcao.trackables.ui.fragment.detail;

import android.os.Bundle;
import android.view.View;

import com.arcao.trackables.R;

public class TrackableMapFragment extends AbstractTrackableFragment {
	public static TrackableMapFragment newInstance(String trackableCode) {
		TrackableMapFragment fragment = new TrackableMapFragment();
		fragment.setTrackableCode(trackableCode);
		return fragment;
	}

	@Override
	protected int getLayout() {
		return R.layout.fragment_trackable_map;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}