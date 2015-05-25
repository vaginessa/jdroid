package com.jdroid.android.navdrawer;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;

import java.util.List;

public abstract class DefaultNavDrawer extends NavDrawer {

	private ListView drawerList;
	private List<NavDrawerItem> visibleNavDrawerItems = Lists.newArrayList();

	public DefaultNavDrawer(AbstractFragmentActivity activity, Boolean darkTheme, Toolbar appBar) {
		super(activity, darkTheme, appBar);

		for (NavDrawerItem each : getNavDrawerItems()) {
			if (each.isVisible()) {
				visibleNavDrawerItems.add(each);
			}
		}
	}

	@Override
	public View createContentView() {

		drawerList = getActivity().findView(R.id.drawer);

		NavDrawerHeaderBuilder navDrawerHeaderBuilder = createNavDrawerHeaderBuilder();
		if (navDrawerHeaderBuilder != null) {
			drawerList.addHeaderView(navDrawerHeaderBuilder.build());
		}
		drawerList.setAdapter(new NavDrawerAdapter(getActivity(), visibleNavDrawerItems));
		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= drawerList.getHeaderViewsCount()) {
					selectNavDrawerItem(position);
				}
			}

		});
		return drawerList;
	}

	private void selectNavDrawerItem(int position) {
		NavDrawerItem navDrawerItem = (NavDrawerItem)drawerList.getAdapter().getItem(position);
		if (!navDrawerItem.matchesActivity(getActivity())) {
			navDrawerItem.startActivity(getActivity());
		}

		// update selected item and title, then close the drawer
		if (navDrawerItem.isMainAction()) {
			drawerList.setItemChecked(position, true);
		} else {
			checkNavDrawerItem();
		}
		getDrawerLayout().closeDrawer(drawerList);
	}

	@Override
	public void onResume() {
		super.onResume();
		checkNavDrawerItem();
	}

	private void checkNavDrawerItem() {
		for (int i = 0; i < visibleNavDrawerItems.size(); i++) {
			NavDrawerItem item = visibleNavDrawerItems.get(i);
			if (item.isMainAction() && item.matchesActivity(getActivity())) {
				drawerList.setItemChecked(i + drawerList.getHeaderViewsCount(), true);
			}
		}
	}

	protected abstract List<NavDrawerItem> getNavDrawerItems();

	protected NavDrawerHeaderBuilder createNavDrawerHeaderBuilder() {
		NavDrawerHeaderBuilder builder = new NavDrawerHeaderBuilder(getActivity());
		User user = SecurityContext.get().getUser();
		if (user != null) {
			builder.setBackground(user.getCoverPictureUrl(), User.PROFILE_PICTURE_TTL);
			builder.setMainImage(user.getProfilePictureUrl(), User.PROFILE_PICTURE_TTL);
			builder.setTitle(user.getFullname());
			builder.setSubTitle(user.getEmail());
		} else {
			builder.setMainImage(R.drawable.ic_launcher);
			builder.setTitle(getActivity().getString(R.string.appName));
			String website = AbstractApplication.get().getAppContext().getWebsite();
			if (website != null) {
				builder.setSubTitle(website.replaceAll("http://", ""));
			}
		}
		return builder;
	}
}
