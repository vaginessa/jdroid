package com.jdroid.android.debug;

import java.util.List;
import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.inappbilling.BillingContext;
import com.jdroid.android.inappbilling.TestProductType;
import com.jdroid.java.collections.Lists;

public class InAppBillingDebugPrefsAppender implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.inAppBillingSettings);
		preferenceScreen.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(BillingContext.IN_APP_BILLING_MOCK_ENABLED);
		checkBoxPreference.setTitle(R.string.inAppBillingMockEnabledTitle);
		checkBoxPreference.setSummary(R.string.inAppBillingMockEnabledDescription);
		preferenceCategory.addPreference(checkBoxPreference);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(BillingContext.IN_APP_BILLING_TEST_PRODUCT_IDS);
		preference.setTitle(R.string.inAppBillingTestProductIdsTitle);
		preference.setDialogTitle(R.string.inAppBillingTestProductIdsTitle);
		preference.setSummary(R.string.inAppBillingTestProductIdsDescription);
		List<CharSequence> entries = Lists.newArrayList();
		for (TestProductType each : TestProductType.values()) {
			entries.add(each.name());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		preferenceCategory.addPreference(preference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().isInAppBillingEnabled();
	}
}
