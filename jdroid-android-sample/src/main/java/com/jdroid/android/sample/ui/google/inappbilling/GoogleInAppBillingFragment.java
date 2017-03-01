package com.jdroid.android.sample.ui.google.inappbilling;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.google.inappbilling.ui.InAppBillingRecyclerFragment;
import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.google.inappbilling.ui.ProductViewType;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;

import java.util.List;

public class GoogleInAppBillingFragment extends InAppBillingRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		showLoading();
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}

	@Override
	public void onProductsLoaded(final List<Product> products) {
		setAdapter(new RecyclerViewAdapter(new ProductViewType() {

			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return GoogleInAppBillingFragment.this;
			}

			@Override
			protected void onPriceClick(Product product) {
				launchPurchaseFlow(product);
			}


		}, products));
		dismissLoading();
	}

	@Override
	public void onPurchased(final Product product) {
		getAdapter().notifyDataSetChanged();
		ToastUtils.showToast(R.string.jdroid_purchaseThanks);
	}
}
