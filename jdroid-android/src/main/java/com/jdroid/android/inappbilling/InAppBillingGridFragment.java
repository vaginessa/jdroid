package com.jdroid.android.inappbilling;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.fragment.AbstractGridFragment;

public abstract class InAppBillingGridFragment extends AbstractGridFragment<Product> implements InAppBillingListener {
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			InAppBillingHelperFragment.add(getActivity(), InAppBillingHelperFragment.class, getManagedProductTypes(),
				getSubscriptionsProductTypes(), false, this);
		}
	}
	
	public List<ProductType> getManagedProductTypes() {
		return AbstractApplication.get().getManagedProductTypes();
	}
	
	public List<ProductType> getSubscriptionsProductTypes() {
		return AbstractApplication.get().getSubscriptionsProductTypes();
	}
	
	public void launchPurchaseFlow(Product product) {
		InAppBillingHelperFragment.get(getActivity()).launchPurchaseFlow(product);
	}
	
	/**
	 * @see com.jdroid.android.inappbilling.InAppBillingListener#onConsumed(com.jdroid.android.inappbilling.Product)
	 */
	@Override
	public void onConsumed(Product product) {
		executeOnUIThread(new Runnable() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
			}
		});
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(getActivity());
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		InAppBillingHelperFragment.removeTarget(getActivity());
	}
}
