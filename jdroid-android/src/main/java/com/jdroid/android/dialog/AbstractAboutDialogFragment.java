package com.jdroid.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.plus.PlusOneButton;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.debug.DebugSettingsActivity;
import com.jdroid.android.share.ShareUtils;
import com.jdroid.android.social.facebook.FacebookAuthenticationFragment;
import com.jdroid.android.social.googleplus.GooglePlusHelperFragment;
import com.jdroid.android.social.googleplus.GooglePlusOneButtonHelper;
import com.jdroid.android.social.twitter.TwitterConnector;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.IntentUtils;
import com.jdroid.java.utils.DateUtils;

public abstract class AbstractAboutDialogFragment extends AbstractDialogFragment {
	
	private GooglePlusOneButtonHelper googlePlusOneButtonHelper;
	
	public void show(Activity activity) {
		FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
		show(fm, getClass().getSimpleName());
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = inflate(R.layout.about_dialog_fragment);
		dialogBuilder.setView(view);
		
		dialogBuilder.setTitle(R.string.about);
		
		dialogBuilder.setPositiveButton(getString(R.string.ok), null);
		
		TextView appName = (TextView)view.findViewById(R.id.appName);
		appName.setText(AbstractApplication.get().getAppName());
		
		TextView version = (TextView)view.findViewById(R.id.version);
		version.setText(getString(R.string.version, AndroidUtils.getVersionName()));
		
		TextView contactUsLabel = (TextView)view.findViewById(R.id.contactUsLabel);
		TextView contactUsEmail = (TextView)view.findViewById(R.id.contactUsEmail);
		final String contactUsEmailAddress = getContactUsEmail();
		if (contactUsEmailAddress != null) {
			contactUsEmail.setText(contactUsEmailAddress);
			contactUsEmail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = ShareUtils.createOpenMail(contactUsEmailAddress,
						AbstractApplication.get().getAppName());
					startActivity(intent);
				}
			});
		} else {
			contactUsLabel.setVisibility(View.GONE);
			contactUsEmail.setVisibility(View.GONE);
			view.findViewById(R.id.contactUsSeparator).setVisibility(View.GONE);
		}
		
		TextView copyright = (TextView)view.findViewById(R.id.copyright);
		copyright.setText(getCopyRightLegend());
		
		TextView allRightsReservedLegend = (TextView)view.findViewById(R.id.allRightsReservedLegend);
		allRightsReservedLegend.setText(getAllRightsReservedLegend());
		
		view.findViewById(R.id.jdroidLegend).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentUtils.startUrl(getActivity(), "http://jdroidframework.com");
			}
		});
		
		View universalImageLoaderLegend = view.findViewById(R.id.universalImageLoaderLegend);
		if (displayUniversalImageLoaderLegend()) {
			universalImageLoaderLegend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IntentUtils.startUrl(getActivity(), "https://github.com/nostra13/Android-Universal-Image-Loader");
				}
			});
		} else {
			universalImageLoaderLegend.setVisibility(View.GONE);
			view.findViewById(R.id.universalImageLoaderLegendSeparator).setVisibility(View.GONE);
		}
		
		PlusOneButton plusOneButton = (PlusOneButton)view.findViewById(R.id.plusOneButton);
		if (displayGooglePlusOneButton()) {
			googlePlusOneButtonHelper = new GooglePlusOneButtonHelper(this, plusOneButton);
		} else {
			plusOneButton.setVisibility(View.GONE);
		}
		
		if (getAppContext().displayDebugSettings()) {
			View debugSettings = view.findViewById(R.id.icon);
			debugSettings.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActivityLauncher.launchActivity(DebugSettingsActivity.class);
				}
			});
		}
		
		Boolean socialEnabled = false;
		View facebook = view.findViewById(R.id.facebook);
		if (getFacebookPageId() != null) {
			facebook.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FacebookAuthenticationFragment.openPage(getFacebookPageId());
				}
			});
			socialEnabled = true;
		} else {
			facebook.setVisibility(View.GONE);
		}
		
		View googlePlus = view.findViewById(R.id.googlePlus);
		if (getGooglePlusCommunityId() != null) {
			googlePlus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GooglePlusHelperFragment.openCommunity(getGooglePlusCommunityId());
				}
			});
			socialEnabled = true;
		} else {
			googlePlus.setVisibility(View.GONE);
		}
		
		View twitter = view.findViewById(R.id.twitter);
		if (getTwitterAccount() != null) {
			twitter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TwitterConnector.openProfile(getTwitterAccount());
				}
			});
			socialEnabled = true;
		} else {
			twitter.setVisibility(View.GONE);
		}
		
		if (!socialEnabled) {
			view.findViewById(R.id.followUs).setVisibility(View.GONE);
		}
		
		View customView = getCustomView();
		ViewGroup customViewContainer = (ViewGroup)view.findViewById(R.id.customViewContainer);
		if (customView != null) {
			customViewContainer.addView(customView);
		} else {
			customViewContainer.setVisibility(View.GONE);
		}
		
		return dialogBuilder.create();
	}
	
	protected String getAllRightsReservedLegend() {
		return getString(R.string.allRightsReservedLegend);
	}
	
	protected String getCopyRightLegend() {
		return getString(R.string.copyright, DateUtils.getYear(), AbstractApplication.get().getAppName());
	}
	
	protected String getContactUsEmail() {
		return AbstractApplication.get().getAppContext().getContactUsEmail();
	}
	
	protected String getFacebookPageId() {
		return null;
	}
	
	protected String getGooglePlusCommunityId() {
		return null;
	}
	
	protected String getTwitterAccount() {
		return null;
	}
	
	protected Boolean displayGooglePlusOneButton() {
		return true;
	}
	
	protected Boolean displayUniversalImageLoaderLegend() {
		return true;
	}
	
	protected View getCustomView() {
		return null;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onResume();
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (googlePlusOneButtonHelper != null) {
			googlePlusOneButtonHelper.onActivityResult(requestCode, resultCode, data);
		}
	}
}
