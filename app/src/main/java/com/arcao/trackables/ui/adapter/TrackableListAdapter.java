package com.arcao.trackables.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.trackables.R;
import com.arcao.trackables.ui.MainActivityComponent;
import com.squareup.picasso.Picasso;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TrackableListAdapter extends RecyclerView.Adapter<TrackableListAdapter.ViewHolder> {
	private final List<Trackable> trackables = new ArrayList<>();

	private MainActivityComponent component;

	public TrackableListAdapter(MainActivityComponent component) {
		this.component = component;
		component.inject(this);
	}

	public void setTrackables(List<Trackable> trackables) {
		this.trackables.clear();
		this.trackables.addAll(trackables);
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trackable_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.bind(trackables.get(position));
	}

	@Override
	public int getItemCount() {
		return trackables.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.image)
		protected ImageView imageView;

		@InjectView(R.id.title)
		protected TextView titleView;

		@InjectView(R.id.trackableCode)
		protected TextView trackableCodeView;

		@Inject
		protected Picasso picasso;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
			component.inject(this);
		}

		public void bind(Trackable trackable) {
			titleView.setText(trackable.getName());
			trackableCodeView.setText(trackable.getTrackingNumber());

			imageView.setScaleType(ImageView.ScaleType.CENTER);

			if (trackable.getImages().size() > 0) {
				Timber.d("Loading image: %s", trackable.getImages().get(0).getThumbUrl());
				picasso.load(trackable.getImages().get(0).getThumbUrl())
								.resize(imageView.getLayoutParams().width, imageView.getLayoutParams().height)
								.centerCrop().into(imageView);
			} else {
				Timber.d("Loading image: %s", trackable.getTrackableTypeImage());
				picasso.load(trackable.getTrackableTypeImage()).resize(dpToPx(32), dpToPx(32)).into(imageView);
			}
		}

		private int dpToPx(int dp) {
			float density = itemView.getResources().getDisplayMetrics().density;
			return Math.round((float)dp * density);
		}
	}
}
