package com.arcao.trackables.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.arcao.geocaching.api.GeocachingApi;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.trackables.R;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.ui.MainActivity;
import com.arcao.trackables.ui.MainActivityComponent;
import com.arcao.trackables.ui.adapter.TrackableListAdapter;
import com.arcao.trackables.ui.widget.recycler_view.SpacesItemDecoration;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

public class TrackableListFragment extends Fragment {

	private MainActivityComponent component;

	@Inject
	GeocachingApi geocachingApi;

	@Inject
	AccountPreferenceHelper accountPreferenceHelper;

	@InjectView(R.id.recyclerView)
	protected RecyclerView mRecyclerView;
	private TrackableListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		component = ((MainActivity)getActivity()).component();
		component.inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trackable_list, container, false);
		ButterKnife.inject(this, view);

		mRecyclerView.setHasFixedSize(true);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addItemDecoration(new SpacesItemDecoration((int)getResources().getDimension(R.dimen.trackable_list_item_space)));

		mAdapter = new TrackableListAdapter(component);
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();


		Observable.create(new Observable.OnSubscribe<List<Trackable>>() {
			@Override
			public void call(Subscriber<? super List<Trackable>> subscriber) {
				try {
					geocachingApi.openSession(accountPreferenceHelper.getAccessToken());
					subscriber.onNext(geocachingApi.getUsersTrackables(0, 30, 0, false));
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}
		}).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(mAdapter::setTrackables);
	}
}
