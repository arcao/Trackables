package com.arcao.trackables.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by sebnapi on 08.11.14.
 * <p/>
 * If you extend this Adapter you are able to add a Header, a Footer or both
 * by a similar ViewHolder pattern as in RecyclerView.
 * <p/>
 * You need to override the abstract methods introduced by this class. This class
 * is not using generics as RecyclerView.Adapter make yourself sure to cast right.
 * <p/>
 */
public abstract class HeaderRecyclerViewAdapter extends RecyclerView.Adapter {
	private static final int TYPE_HEADER = Integer.MIN_VALUE;
	private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
	private static final int TYPE_ADAPTEE_OFFSET = 2;

	@Override
	public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_HEADER) {
			return onCreateHeaderViewHolder(parent, viewType);
		} else if (viewType == TYPE_FOOTER) {
			return onCreateFooterViewHolder(parent, viewType);
		}
		return onCreateBasicItemViewHolder(parent, viewType - TYPE_ADAPTEE_OFFSET);
	}

	@Override
	public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
			onBindHeaderView(holder, position);
		} else if (position == getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
			onBindFooterView(holder, position);
		} else {
			onBindBasicItemView(holder, position - (useHeader() ? 1 : 0));
		}
	}

	@Override
	public final int getItemCount() {
		int itemCount = getBasicItemCount();
		if (useHeader()) {
			itemCount += 1;
		}
		if (useFooter()) {
			itemCount += 1;
		}
		return itemCount;
	}

	@Override
	public final int getItemViewType(int position) {
		if (position == 0 && useHeader()) {
			return TYPE_HEADER;
		}
		if (position == getBasicItemCount() && useFooter()) {
			return TYPE_FOOTER;
		}
		if (getBasicItemType(position) >= Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
			new IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by " + TYPE_ADAPTEE_OFFSET + ".");
		}
		return getBasicItemType(position) + TYPE_ADAPTEE_OFFSET;
	}

	public abstract boolean useHeader();

	public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

	public abstract void onBindHeaderView(RecyclerView.ViewHolder holder, int position);

	public abstract boolean useFooter();

	public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

	public abstract void onBindFooterView(RecyclerView.ViewHolder holder, int position);

	public abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

	public abstract void onBindBasicItemView(RecyclerView.ViewHolder holder, int position);

	public abstract int getBasicItemCount();

	/**
	 * make sure you don't use [Integer.MAX_VALUE-1, Integer.MAX_VALUE] as BasicItemViewType
	 *
	 * @param position
	 * @return
	 */
	public abstract int getBasicItemType(int position);

}