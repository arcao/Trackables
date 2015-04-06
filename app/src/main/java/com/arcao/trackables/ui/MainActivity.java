package com.arcao.trackables.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.arcao.trackables.R;
import com.arcao.trackables.preference.AccountPreferenceHelper;
import com.arcao.trackables.preference.PreferenceHelper;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

import javax.inject.Inject;

public class MainActivity extends ActionBarActivity {
	@Inject
	AccountPreferenceHelper accountPreferenceHelper;

	@Inject
	PreferenceHelper preferenceHelper;

	private AccountHeader.Result headerResult = null;
	private Drawer.Result result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityComponent.Initializer.init(this).inject(this);

		/*if (!accountPreferenceHelper.isAccount()) {
			startActivity(new Intent(this, WelcomeActivity.class));
			finish();
			return;
		}*/

		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final IProfile profile = new ProfileDrawerItem().withName("Arcao").withEmail("arcao@arcao.com").withIcon("http://example.com/icon.png");

		// Create the AccountHeader
		headerResult = new AccountHeader()
						.withActivity(this)
						.withHeaderBackground(R.drawable.header)
						.withSelectionListEnabledForSingleProfile(false)
						.withCompactStyle(true)
						.addProfiles(
										profile
						)
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
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState = result.saveInstanceState(outState);
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
