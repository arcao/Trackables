package com.arcao.trackables.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arcao.trackables.R;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.DetailActivityComponent;
import com.arcao.trackables.ui.adapter.DetailPagerAdapter;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends AppCompatActivity implements HasComponent<DetailActivityComponent> {
	private static final String PARAM__TRACKING_NUMBER = "TRACKING_NUMBER";

	@InjectView(R.id.toolbar)
	protected Toolbar mToolbar;

	@InjectView(R.id.viewpager)
	protected ViewPager mViewPager;

	@InjectView(R.id.sliding_tabs)
	protected PagerSlidingTabStrip mPagerSlidingTabStrip;

	private DetailActivityComponent component;
	public DetailActivityComponent component() {
		if (component == null)
			component = DetailActivityComponent.Initializer.init(this);
		return component;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		component().inject(this);
		setContentView(R.layout.activity_detail);
		ButterKnife.inject(this);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mViewPager.setAdapter(new DetailPagerAdapter(this, getFragmentManager(), getIntent().getStringExtra(PARAM__TRACKING_NUMBER)));
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static Intent createIntent(Context context, String trackableCode) {
		return new Intent(context, DetailActivity.class).putExtra(PARAM__TRACKING_NUMBER, trackableCode);
	}
}
