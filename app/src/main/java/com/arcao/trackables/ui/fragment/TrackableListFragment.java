package com.arcao.trackables.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.data.service.DataSource;
import com.arcao.trackables.data.service.TrackableService;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.MainActivityComponent;
import com.arcao.trackables.internal.rx.AndroidSchedulerTransformer;
import com.arcao.trackables.ui.MainActivity;
import com.arcao.trackables.ui.adapter.TrackableListAdapter;
import com.arcao.trackables.ui.widget.recycler_view.SpacesItemDecoration;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.internal.util.SubscriptionList;

public class TrackableListFragment extends Fragment implements HasComponent<MainActivityComponent> {
	@Inject
	protected AccountService accountService;

	@Inject
	protected TrackableService trackableService;

	@Inject
	protected ExceptionHandler exceptionHandler;

	@InjectView(R.id.recyclerView)
	protected RecyclerView mRecyclerView;
	@Inject
	protected TrackableListAdapter mAdapter;

	private final SubscriptionList subscriptionList = new SubscriptionList();

	public MainActivityComponent component() {
		return ((MainActivity)getActivity()).component();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		component().inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trackable_list, container, false);
		ButterKnife.inject(this, view);

		mRecyclerView.setHasFixedSize(true);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.trackable_list_item_space)));
		mRecyclerView.setAdapter(mAdapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (accountService.isAccount()) {
			subscriptionList.add(trackableService.getUserTrackables(DataSource.REMOTE_IF_NECESSARY)
							.compose(AndroidSchedulerTransformer.get())
							.subscribe(mAdapter::setTrackables, throwable -> startActivity(exceptionHandler.handle(throwable))));
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		subscriptionList.unsubscribe();
	}
}
