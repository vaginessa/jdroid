package com.jdroid.android.sample.ui.hero;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class HeroFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.hero_fragment;
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
