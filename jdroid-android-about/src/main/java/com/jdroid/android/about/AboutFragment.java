package com.jdroid.android.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.feedback.RateAppStats;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.recycler.SimpleRecyclerViewType;
import com.jdroid.android.share.ShareUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;

import java.util.List;

public class AboutFragment extends AbstractRecyclerFragment {
	
	private List<Object> aboutItems = Lists.newArrayList();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Header
		aboutItems.add(new Object());

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

		if (AboutAppModule.get().getAboutContext().getSpreadTheLoveFragmentClass() != null) {
			aboutItems.add(new AboutItem(R.drawable.ic_spread_the_love, R.string.spreadTheLove) {

				@Override
				public void onSelected(Activity activity) {
					ActivityLauncher.launchActivity(SpreadTheLoveActivity.class);
				}
			});
		}
		aboutItems.add(new AboutItem(R.drawable.ic_libraries, R.string.libraries) {
			
			@Override
			public void onSelected(Activity activity) {
				ActivityLauncher.launchActivity(LibrariesActivity.class);
			}
		});
		aboutItems.addAll(getCustomAboutItems());

		if (rateAppViewEnabled() && RateAppStats.displayRateAppView()) {
			// Footer
			aboutItems.add(new Object());
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<RecyclerViewType> recyclerViewTypes = Lists.newArrayList();
		recyclerViewTypes.add(new HeaderRecyclerViewType());
		recyclerViewTypes.add(new AboutRecyclerViewType());
		recyclerViewTypes.add(new FooterRecyclerViewType());

		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, aboutItems));
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

	protected List<AboutItem> getCustomAboutItems() {
		return Lists.newArrayList();
	}

	protected Boolean rateAppViewEnabled() {
		return true;
	}

	@Override
	protected Boolean isDividerItemDecorationEnabled() {
		return true;
	}

	public class HeaderRecyclerViewType extends RecyclerViewType<Object, HeaderItemHolder> {

		@Override
		protected Class<Object> getItemClass() {
			return Object.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.about_header_view;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			HeaderItemHolder holder = new HeaderItemHolder(view);
			holder.appName = findView(view, R.id.appName);
			holder.version = findView(view, R.id.version);
			holder.copyright = findView(view, R.id.copyright);
			if (getAppContext().displayDebugSettings()) {
				holder.debugSettings = findView(view, R.id.icon);
			}
			return holder;
		}

		@Override
		public void fillHolderFromItem(Object o, HeaderItemHolder holder) {
			holder.appName.setText(AbstractApplication.get().getAppName());
			holder.version.setText(getString(R.string.version, AndroidUtils.getVersionName()));
			holder.copyright.setText(getCopyRightLegend());
			if (getAppContext().displayDebugSettings()) {
				holder.debugSettings.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AbstractApplication.get().getDebugContext().launchActivityDebugSettingsActivity();
					}
				});
			}
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return AboutFragment.this;
		}
	}

	public static class HeaderItemHolder extends RecyclerView.ViewHolder {

		protected TextView appName;
		protected TextView version;
		protected TextView copyright;
		protected View debugSettings;

		public HeaderItemHolder(View itemView) {
			super(itemView);
		}
	}

	public class FooterRecyclerViewType extends SimpleRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.about_footer_view;
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return AboutFragment.this;
		}
	}

	public class AboutRecyclerViewType extends RecyclerViewType<AboutItem, AboutItemHolder> {

		@Override
		protected Class<AboutItem> getItemClass() {
			return AboutItem.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.default_item;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			AboutItemHolder holder = new AboutItemHolder(view);
			holder.image = findView(view, R.id.image);
			holder.name = findView(view, R.id.name);
			return holder;
		}

		@Override
		public void fillHolderFromItem(AboutItem item, AboutItemHolder holder) {
			holder.image.setImageResource(item.getIconResId());
			holder.name.setText(item.getNameResId());
		}

		@Override
		public void onItemSelected(AboutItem item, View view) {
			item.onSelected(getActivity());
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return AboutFragment.this;
		}
	}

	public static class AboutItemHolder extends RecyclerView.ViewHolder {

		protected ImageView image;
		protected TextView name;

		public AboutItemHolder(View itemView) {
			super(itemView);
		}
	}
}
