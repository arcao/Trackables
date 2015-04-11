package com.arcao.trackables.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.arcao.geocaching.api.data.Trackable;
import com.arcao.trackables.R;
import com.arcao.trackables.ui.MainActivityComponent;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.squareup.picasso.Picasso;
import org.apache.commons.lang3.StringUtils;
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
		protected TextView titleTextView;

		@InjectView(R.id.trackableCodeText)
		protected TextView trackableCodeTextView;

		@InjectView(R.id.positionText)
		protected TextView positionTextView;

		@Inject
		protected Picasso picasso;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
			component.inject(this);
		}

		public void bind(Trackable trackable) {
			titleTextView.setText(trackable.getName());



			applyIcon(trackableCodeTextView, GoogleMaterial.Icon.gmd_label);
			trackableCodeTextView.setText(trackable.getTrackingNumber());

			imageView.setScaleType(ImageView.ScaleType.CENTER);

			if (trackable.getImages().size() > 0) {
				Timber.d("Loading image: %s", trackable.getImages().get(0).getUrl());
				picasso.load(trackable.getImages().get(0).getUrl())
								.resize(imageView.getLayoutParams().width, imageView.getLayoutParams().height)
								.centerCrop().into(imageView);
			} else {
				Timber.d("Loading image: %s", trackable.getTrackableTypeImage());
				picasso.load(trackable.getTrackableTypeImage()).resize(dpToPx(32), dpToPx(32)).into(imageView);
			}


			if (!StringUtils.isEmpty(trackable.getCurrentCacheCode())) {
				applyIcon(positionTextView, GoogleMaterial.Icon.gmd_place);
				positionTextView.setText(trackable.getCurrentCacheCode());
			} else if (trackable.getCurrentOwner() != null) {
				applyIcon(positionTextView, GoogleMaterial.Icon.gmd_person);
				positionTextView.setText(trackable.getCurrentOwner().getUserName());
			}
		}

		private void applyIcon(TextView target, IIcon icon) {
			target.setCompoundDrawables(
							new IconicsDrawable(target.getContext(), icon).color(target.getCurrentTextColor()).sizeDp(10).iconOffsetYDp(1), null, null, null);
		}

		private int dpToPx(int dp) {
			float density = itemView.getResources().getDisplayMetrics().density;
			return Math.round((float)dp * density);
		}
	}
}