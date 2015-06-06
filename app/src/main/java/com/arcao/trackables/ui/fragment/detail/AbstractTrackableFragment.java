package com.arcao.trackables.ui.fragment.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.DetailActivityComponent;
import com.arcao.trackables.ui.DetailActivity;

import butterknife.ButterKnife;

public abstract class AbstractTrackableFragment  extends Fragment implements HasComponent<DetailActivityComponent> {
	private static final String ARG_TRACKABLE_CODE = "TRACKABLE_CODE";

	protected void setTrackableCode(String trackableCode) {
		if (getArguments() == null) {
			setArguments(new Bundle());
		}

		getArguments().putString(ARG_TRACKABLE_CODE, trackableCode);
	}

	protected String getTrackableCode() {
		return getArguments().getString(ARG_TRACKABLE_CODE);
	}

	@LayoutRes
	protected abstract int getLayout();

	@Override
	public DetailActivityComponent component() {
		return ((DetailActivity) getActivity()).component();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		component().inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayout(), container, false);
		ButterKnife.inject(this, view);
		return view;
	}
}
