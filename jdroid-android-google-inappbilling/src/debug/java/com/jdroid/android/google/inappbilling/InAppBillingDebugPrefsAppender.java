package com.jdroid.android.google.inappbilling;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class InAppBillingDebugPrefsAppender implements PreferencesAppender {
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.inAppBillingSettings);
		preferenceGroup.addPreference(preferenceCategory);
		
		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(InAppBillingContext.MOCK_ENABLED);
		checkBoxPreference.setTitle(R.string.inAppBillingMockEnabledTitle);
		checkBoxPreference.setSummary(R.string.inAppBillingMockEnabledDescription);
		preferenceCategory.addPreference(checkBoxPreference);
		
		ListPreference preference = new ListPreference(activity);
		preference.setKey(InAppBillingContext.TEST_PRODUCT_IDS);
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
		
		// Purchased products
		List<ProductType> purchasedProductTypes = InAppBillingAppModule.get().getInAppBillingContext().getPurchasedProductTypes();
		if (!purchasedProductTypes.isEmpty()) {
			preference = new ListPreference(activity);
			preference.setTitle(R.string.inAppBillingPurchasedProductTypeTitle);
			preference.setDialogTitle(R.string.inAppBillingPurchasedProductTypeTitle);
			preference.setSummary(R.string.inAppBillingPurchasedProductTypeTitle);
			entries = Lists.newArrayList();
			for (ProductType each : InAppBillingAppModule.get().getInAppBillingContext().getPurchasedProductTypes()) {
				entries.add(each.getProductId());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preferenceCategory.addPreference(preference);
		}
	}
	
	@Override
	public Boolean isEnabled() {
		return !InAppBillingAppModule.get().getInAppBillingContext().getManagedProductTypes().isEmpty()
				|| !InAppBillingAppModule.get().getInAppBillingContext().getSubscriptionsProductTypes().isEmpty();
	}
}
