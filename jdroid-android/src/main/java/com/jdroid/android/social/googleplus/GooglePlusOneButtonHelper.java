package com.jdroid.android.social.googleplus;

import java.util.Locale;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener;
import com.jdroid.android.utils.GooglePlayUtils;

public class GooglePlusOneButtonHelper {
	
	private static final int PLUS_ONE_REQUEST_CODE = 100;
	private static final int PLUS_ONE_UNDO_REQUEST_CODE = 101;
	private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";
	
	private String url;
	protected PlusOneButton plusOneButton;
	private Fragment context;
	
	public GooglePlusOneButtonHelper(Fragment context, int plusOneButton) {
		this(context, (PlusOneButton)context.getView().findViewById(plusOneButton));
	}
	
	public GooglePlusOneButtonHelper(Fragment context, PlusOneButton plusOneButton) {
		this.context = context;
		this.plusOneButton = plusOneButton;
		url = getUrl();
	}
	
	public void onResume() {
		
		if (GooglePlayUtils.isGooglePlayServicesAvailable(context.getActivity())) {
			plusOneButton.initialize(url, new OnPlusOneClickListener() {
				
				@Override
				public void onPlusOneClick(Intent intent) {
					if (intent != null) {
						if (intent.getAction().toLowerCase(Locale.US).contains("undo")) {
							context.startActivityForResult(intent, PLUS_ONE_UNDO_REQUEST_CODE);
						} else {
							context.startActivityForResult(intent, PLUS_ONE_REQUEST_CODE);
						}
					}
				}
			});
			plusOneButton.setVisibility(View.VISIBLE);
		} else {
			plusOneButton.setVisibility(View.GONE);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == PLUS_ONE_UNDO_REQUEST_CODE) && (resultCode != 0)) {
			onUndoPlusOne();
		} else if ((requestCode == PLUS_ONE_REQUEST_CODE) && (resultCode != 0)) {
			onPlusOne();
		}
	}
	
	protected void onPlusOne() {
		// Do Nothing
	}
	
	protected void onUndoPlusOne() {
		// Do Nothing
	}
	
	protected String getUrl() {
		return PLAY_STORE_BASE_URL + context.getActivity().getPackageName();
	}
}
