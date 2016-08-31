package com.jdroid.android.navdrawer;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;

import java.util.List;

public abstract class DefaultNavDrawer extends NavDrawer {

	private NavigationView navigationView;

	public DefaultNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		super(activity, appBar);
	}

	@Override
	public View createContentView() {
		navigationView = getActivity().findView(R.id.drawer);
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						DefaultNavDrawer.this.onNavigationItemSelected(menuItem);
						menuItem.setChecked(true);
						getDrawerLayout().closeDrawers();
						return true;
					}
				});
		NavDrawerHeader navDrawerHeader = new NavDrawerHeader(navigationView);
		initNavDrawerHeader(navDrawerHeader);
		return navigationView;
	}

	protected void onNavigationItemSelected(MenuItem menuItem) {
		findNavDrawerByMenu(menuItem).startActivity();
	}

	@Override
	public void onResume() {
		super.onResume();

		checkMenuItem();
	}

	protected void checkMenuItem() {
		for (int i = 0; i < navigationView.getMenu().size(); i++) {
			MenuItem menuItem = navigationView.getMenu().getItem(i);
			NavDrawerItem navDrawerItem = findNavDrawerByMenu(menuItem);
			if (AbstractApplication.get().getCurrentActivity().getClass().equals(navDrawerItem.getActivityClass())) {
				menuItem.setChecked(true);
				break;
			}
		}
	}

	private NavDrawerItem findNavDrawerByMenu(MenuItem menuItem) {
		for (NavDrawerItem each: getNavDrawerItems()) {
			if (each.getItemId().equals(menuItem.getItemId())) {
				return each;
			}
		}
		return null;
	}

	protected abstract List<NavDrawerItem> getNavDrawerItems();

	protected void initNavDrawerHeader(NavDrawerHeader navDrawerHeader) {
		User user = SecurityContext.get().getUser();
		if (user != null) {
			navDrawerHeader.setBackground(user.getCoverPictureUrl(), User.PROFILE_PICTURE_TTL);
			navDrawerHeader.setMainImage(user.getProfilePictureUrl(), User.PROFILE_PICTURE_TTL);
			navDrawerHeader.setTitle(user.getFullname());
			navDrawerHeader.setSubTitle(user.getEmail());
		} else {
			navDrawerHeader.setMainImage(AbstractApplication.get().getLauncherIconResId());
			navDrawerHeader.setTitle(getActivity().getString(R.string.jdroid_appName));
			String website = AbstractApplication.get().getAppContext().getWebsite();
			if (website != null) {
				navDrawerHeader.setSubTitle(website.replaceAll("http://", ""));
			}
		}
	}
}
