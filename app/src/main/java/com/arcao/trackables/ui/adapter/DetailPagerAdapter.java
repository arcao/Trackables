package com.arcao.trackables.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.arcao.trackables.R;
import com.arcao.trackables.ui.fragment.detail.TrackableDetailFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableLogsFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableMapFragment;
import com.arcao.trackables.ui.fragment.detail.TrackableStatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends FragmentPagerAdapter {
	private final Context mContext;
	private final List<Fragment> detailFragments = new ArrayList<>();

	public DetailPagerAdapter(Context context, FragmentManager fm, String trackableCode) {
		super(fm);

		mContext = context;

		detailFragments.add(TrackableDetailFragment.newInstance(trackableCode));
		detailFragments.add(TrackableLogsFragment.newInstance(trackableCode));
		detailFragments.add(TrackableMapFragment.newInstance(trackableCode));
		detailFragments.add(TrackableStatisticsFragment.newInstance(trackableCode));
	}

	@Override
	public Fragment getItem(int position) {
		return detailFragments.get(position);
	}

	@Override
	public int getCount() {
		return detailFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getStringArray(R.array.trackable_detail_tabs)[position];
	}
}
