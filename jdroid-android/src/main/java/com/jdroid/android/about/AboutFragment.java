package com.jdroid.android.about;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.ad.RateAppView;
import com.jdroid.android.debug.DebugSettingsActivity;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.share.ShareUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;

// TODO See reconstruction and rotation for all the about screens
public class AboutFragment extends AbstractListFragment<AboutItem> {
	
	private List<AboutItem> aboutItems = Lists.newArrayList();
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final String contactUsEmailAddress = getContactUsEmail();
		if (contactUsEmailAddress != null) {
			aboutItems.add(new AboutItem(R.drawable.ic_contact_us, R.string.contactUs) {
				
				@Override
				public void onSelected(Activity activity) {
					Intent intent = ShareUtils.createOpenMail(contactUsEmailAddress,
						AbstractApplication.get().getAppName());
					startActivity(intent);
					AbstractApplication.get().getAnalyticsSender().trackContactUs();
				}
			});
		}
		aboutItems.add(new AboutItem(R.drawable.ic_spread_the_love, R.string.spreadTheLove) {
			
			@Override
			public void onSelected(Activity activity) {
				ActivityLauncher.launchActivity(SpreadTheLoveActivity.class);
			}
		});
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
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.separated_list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
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
					ActivityLauncher.launchActivity(DebugSettingsActivity.class);
				}
			});
		}
		
		getListView().addHeaderView(header);
		setListAdapter(new AboutItemsAdapter(getActivity(), aboutItems));
	}
	
	protected String getCopyRightLegend() {
		return getString(R.string.copyright, DateUtils.getYear(), AbstractApplication.get().getAppName());
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
