package com.jdroid.android.debug;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractPreferenceFragment;

/**
 * 
 * @author Maxi Rosson
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DebugSettingsFragment extends AbstractPreferenceFragment {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preferences);
	}
	
	/**
	 * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		View debugInfoView = new DebugInfoView(getActivity());
		
		ListView listView = ((ListView)findView(android.R.id.list));
		listView.addFooterView(debugInfoView);
	}
}
