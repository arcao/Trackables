package com.arcao.trackables.ui.fragment.detail;

import android.os.Bundle;
import android.view.View;

import com.arcao.trackables.R;

public class TrackableLogsFragment extends AbstractTrackableFragment {
	public static TrackableLogsFragment newInstance(String trackableCode) {
		TrackableLogsFragment fragment = new TrackableLogsFragment();
		fragment.setTrackableCode(trackableCode);
		return fragment;
	}

	@Override
	protected int getLayout() {
		return R.layout.fragment_trackable_logs;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}
