package com.arcao.trackables.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.arcao.geocaching.api.data.type.MemberType;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.MainActivityComponent;
import com.arcao.trackables.ui.fragment.TrackableListFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import timber.log.Timber;

import javax.inject.Inject;

public class MainActivity extends ActionBarActivity implements HasComponent<MainActivityComponent> {
	@Inject
	protected AccountService accountService;

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	private AccountHeader.Result headerResult = null;
	private Drawer.Result result = null;

	private MainActivityComponent component;
	public MainActivityComponent component() {
		if (component == null)
			component = MainActivityComponent.Initializer.init(this);
		return component;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		component().inject(this);

		setContentView(R.layout.activity_main);

		ButterKnife.inject(this);
		setSupportActionBar(toolbar);

		// Create the AccountHeader
		headerResult = new AccountHeader()
						.withActivity(this)
						.withHeaderBackground(R.drawable.header)
						.withSelectionListEnabledForSingleProfile(false)
						.withCompactStyle(true)
						.withOnAccountHeaderListener((view, profile1, current) -> {
							if (profile1 instanceof ProfileSettingDrawerItem) {
								return false;
							}
							return false;
						})
						.withSavedInstance(savedInstanceState)
						.build();


		//Create the drawer
		result = new Drawer()
						.withActivity(this)
						.withToolbar(toolbar)
						.withAccountHeader(headerResult)
						.addDrawerItems(
										new PrimaryDrawerItem().withName(R.string.drawer_item_own).withIcon(FontAwesome.Icon.faw_home),
										new PrimaryDrawerItem().withName(R.string.drawer_item_favorited).withIcon(FontAwesome.Icon.faw_gamepad),
										new DividerDrawerItem(),
										new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false)
						) // add the items we want to use with our Drawer
						.withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> {
							if (drawerItem != null && drawerItem instanceof Nameable) {
								getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
							}
						})
						.withOnDrawerListener(new Drawer.OnDrawerListener() {
							@Override
							public void onDrawerOpened(View drawerView) {
								KeyboardUtil.hideKeyboard(MainActivity.this);
							}
							@Override
							public void onDrawerClosed(View drawerView) {
							}
						})
						.withFireOnInitialOnClick(true)
						.withSavedInstance(savedInstanceState)
						.build();

		//react on the keyboard
		//result.keyboardSupportEnabled(this, true);

		getFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrackableListFragment()).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		headerResult.clear();
		headerResult.addProfiles(createProfile());

		if (!accountService.isAccount()) {
			startActivity(new Intent(this, WelcomeActivity.class));
		}
	}

	private ProfileDrawerItem createProfile() {
		if (accountService.isAccount()) {
			Timber.d(accountService.getAvatarUrl());
			return new ProfileDrawerItem()
							.withName(accountService.getUserName())
							.withEmail(accountService.getMemberType() == MemberType.Premium ? getString(R.string.member_premium) : getString(R.string.member_basic))
							.withIcon(accountService.getAvatarUrl());
		} else {
			return new ProfileDrawerItem();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState = result.saveInstanceState(outState);
		outState = headerResult.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onBackPressed() {
		if (result != null && result.isDrawerOpen()) {
			result.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}
}
