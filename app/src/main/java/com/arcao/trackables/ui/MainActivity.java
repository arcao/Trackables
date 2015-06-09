package com.arcao.trackables.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arcao.geocaching.api.data.type.MemberType;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.MainActivityComponent;
import com.arcao.trackables.ui.fragment.TrackableListFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements HasComponent<MainActivityComponent> {
	private static final int ID_MY = 0;
	private static final int ID_FAVORITE = 1;
	private static final int ID_SETTINGS = -1;

	@Inject
	protected AccountService accountService;

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	private AccountHeader accountHeader = null;
	private Drawer drawer = null;

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
		accountHeader = new AccountHeaderBuilder()
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
		drawer = new DrawerBuilder()
						.withActivity(this)
						.withToolbar(toolbar)
						.withAccountHeader(accountHeader)
						.addDrawerItems(
										new PrimaryDrawerItem().withName(R.string.drawer_item_own).withIdentifier(ID_MY).withIcon(FontAwesome.Icon.faw_heart),
										new PrimaryDrawerItem().withName(R.string.drawer_item_favorited).withIdentifier(ID_FAVORITE).withIcon(FontAwesome.Icon.faw_star)
						) // add the items we want to use with our Drawer
						.withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> drawerItem != null && drawerItem instanceof Nameable && onClickDrawerItem(drawerItem))
						.withOnDrawerListener(new Drawer.OnDrawerListener() {
							@Override
							public void onDrawerOpened(View drawerView) {
								KeyboardUtil.hideKeyboard(MainActivity.this);
							}
							@Override
							public void onDrawerClosed(View drawerView) {
							}
							@Override
							public void onDrawerSlide(View view, float v) {
							}
						})
						.addStickyDrawerItems(
										new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIdentifier(ID_SETTINGS).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false)
						)
						.withFireOnInitialOnClick(true)
						.withAnimateDrawerItems(true)
						.withCloseOnClick(true)
						.withSavedInstance(savedInstanceState)
						.build();

		//react on the keyboard
		drawer.keyboardSupportEnabled(this, true);

		getFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrackableListFragment()).commit();
	}

	private boolean onClickDrawerItem(IDrawerItem drawerItem) {
		switch (drawerItem.getIdentifier()) {
			case ID_SETTINGS:
				// show settings
				return true;
			default:
				setTitle(((Nameable)drawerItem).getNameRes());
				return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		accountHeader.clear();
		accountHeader.addProfiles(createProfile());

		if (!accountService.isAccount()) {
			startActivity(new Intent(this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			finish();
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
		outState = drawer.saveInstanceState(outState);
		outState = accountHeader.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onBackPressed() {
		if (drawer != null && drawer.isDrawerOpen()) {
			drawer.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}
}
