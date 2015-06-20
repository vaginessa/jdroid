package com.jdroid.android.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.ad.RateAppView;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.share.ShareUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.android.utils.IntentUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;

import java.util.List;

public class AboutFragment extends AbstractListFragment<AboutItem> {
	
	private List<AboutItem> aboutItems = Lists.newArrayList();
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String website = getWebsite();
		if (website != null) {
			aboutItems.add(new AboutItem(R.drawable.ic_website, R.string.website) {
				@Override
				public void onSelected(Activity activity) {
					IntentUtils.startUrl(activity, website);
				}
			});
		}

		final String contactUsEmailAddress = getContactUsEmail();
		if (contactUsEmailAddress != null) {
			aboutItems.add(new AboutItem(R.drawable.ic_contact_us, R.string.contactUs) {

				@Override
				public void onSelected(Activity activity) {
					Intent intent = ShareUtils.createOpenMail(contactUsEmailAddress,
						AbstractApplication.get().getAppName());
					if (IntentUtils.isIntentAvailable(intent)) {
						startActivity(intent);
						AbstractApplication.get().getAnalyticsSender().trackContactUs();
					} else {
						// TODO Improve this adding a toast or something
						AbstractApplication.get().getExceptionHandler().logWarningException(
							"Error when sending email intent");
					}
				}
			});
		}

		if (AbstractApplication.get().getSpreadTheLoveFragmentClass() != null) {
			aboutItems.add(new AboutItem(R.drawable.ic_spread_the_love, R.string.spreadTheLove) {

				@Override
				public void onSelected(Activity activity) {
					ActivityLauncher.launchActivity(SpreadTheLoveActivity.class);
				}
			});
		}
		aboutItems.add(new AboutItem(R.drawable.ic_rate_us, R.string.rateUs) {
			
			@Override
			public void onSelected(Activity activity) {
				RateAppView.rateMeClicked();
				GooglePlayUtils.launchAppDetails(getActivity());
				AbstractApplication.get().getAnalyticsSender().trackRateUs();
			}
		});
		aboutItems.add(new AboutItem(R.drawable.ic_libraries, R.string.libraries) {
			
			@Override
			public void onSelected(Activity activity) {
				ActivityLauncher.launchActivity(LibrariesActivity.class);
			}
		});
		aboutItems.addAll(getCustomAboutItems());
	}
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.separated_list_fragment;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		View header = inflate(R.layout.about_header_fragment);
		TextView appName = (TextView)header.findViewById(R.id.appName);
		appName.setText(AbstractApplication.get().getAppName());
		
		TextView version = (TextView)header.findViewById(R.id.version);
		version.setText(getString(R.string.version, AndroidUtils.getVersionName()));
		
		TextView copyright = (TextView)header.findViewById(R.id.copyright);
		copyright.setText(getCopyRightLegend());
		
		if (getAppContext().displayDebugSettings()) {
			View debugSettings = header.findViewById(R.id.icon);
			debugSettings.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AbstractApplication.get().getDebugContext().launchActivityDebugSettingsActivity();
				}
			});
		}

		if (getListView().getHeaderViewsCount() == 0) {
			if (getListAdapter() != null) {
				setListAdapter(null);
			}
			getListView().addHeaderView(header);
		}
		if (getListAdapter() == null) {
			setListAdapter(new AboutItemsAdapter(getActivity(), aboutItems));
		}
	}
	
	protected String getCopyRightLegend() {
		return getString(R.string.copyright, DateUtils.getYear(), AbstractApplication.get().getAppName());
	}
	
	protected String getWebsite() {
		return AbstractApplication.get().getAppContext().getWebsite();
	}

	protected String getContactUsEmail() {
		return AbstractApplication.get().getAppContext().getContactUsEmail();
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(AboutItem item) {
		item.onSelected(getActivity());
	}
	
	protected List<AboutItem> getCustomAboutItems() {
		return Lists.newArrayList();
	}
}
