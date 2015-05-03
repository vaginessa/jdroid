package com.jdroid.sample.android.hero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.sample.android.R;

public class HeroFragment extends AbstractFragment {

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup,
	 *      Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.hero_fragment, container, false);
	}

	@Override
	protected Boolean isHeroImageEnabled() {
		return true;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getHeroImageId()
	 */
	@Override
	protected Integer getHeroImageId() {
		return R.id.heroImage;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getHeroImageContainerId()
	 */
	@Override
	protected Integer getHeroImageContainerId() {
		return R.id.heroImageContainer;
	}

	@Override
	protected Integer getParallaxScrollViewId() {
		return R.id.scrollView;
	}
}
