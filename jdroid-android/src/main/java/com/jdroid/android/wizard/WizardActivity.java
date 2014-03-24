package com.jdroid.android.wizard;

import java.util.List;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.view.CustomViewPager;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class WizardActivity extends AbstractFragmentActivity {
	
	private CustomViewPager pager;
	private Button leftButton;
	private Button rightButton;
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return isNavDrawerEnabled() ? R.layout.nav_wizard_activity : R.layout.wizard_activity;
	}
	
	protected void loadWizard() {
		loadWizard(null);
	}
	
	protected void loadWizard(Bundle fragmentsBundle) {
		WizardStepFragmentAdapter pagerAdapter = new WizardStepFragmentAdapter(getSupportFragmentManager(),
				getWizardSteps(), fragmentsBundle);
		
		pager = findView(R.id.pager);
		pager.setAdapter(pagerAdapter);
		if (getOffscreenPageLimit() != null) {
			pager.setOffscreenPageLimit(getOffscreenPageLimit());
		}
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateBottomBar();
			}
		});
		
		leftButton = findView(R.id.leftButton);
		leftButton.setText(getLeftStringResId());
		leftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		});
		
		rightButton = findView(R.id.rightButton);
		rightButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (isOnFinishStep()) {
					onfinishWizard();
				} else {
					pager.setCurrentItem(pager.getCurrentItem() + 1);
				}
			}
		});
		
		updateBottomBar();
	}
	
	public abstract List<? extends WizardStep> getWizardSteps();
	
	protected Integer getOffscreenPageLimit() {
		return null;
	};
	
	protected int getLeftStringResId() {
		return R.string.previous;
	}
	
	protected int getRightStringResId() {
		return R.string.next;
	}
	
	protected int getFinishStringResId() {
		return R.string.finish;
	}
	
	public void enableLeft() {
		leftButton.setEnabled(true);
		pager.setPagingEnabled(true);
	}
	
	public void disableLeft() {
		leftButton.setEnabled(false);
		pager.setPagingEnabled(false);
	}
	
	public void enableRight() {
		rightButton.setEnabled(true);
		pager.setPagingEnabled(true);
	}
	
	public void disableRight() {
		rightButton.setEnabled(false);
		pager.setPagingEnabled(false);
	}
	
	public WizardStep getCurrentWizardStep() {
		return getWizardSteps().get(pager.getCurrentItem());
	}
	
	private void updateBottomBar() {
		int position = pager.getCurrentItem();
		if (isOnFinishStep()) {
			rightButton.setText(getFinishStringResId());
			if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN) {
				rightButton.setBackgroundResource(R.drawable.finish_background);
				rightButton.setTextAppearance(this, R.style.finishWizardText);
			}
		} else {
			rightButton.setText(getRightStringResId());
			if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN) {
				rightButton.setBackgroundResource(R.drawable.default_item_selector);
				TypedValue v = new TypedValue();
				getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
				rightButton.setTextAppearance(this, v.resourceId);
			}
		}
		
		leftButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}
	
	private Boolean isOnFinishStep() {
		return pager.getCurrentItem() == (getWizardSteps().size() - 1);
	}
	
	protected abstract void onfinishWizard();
	
}
