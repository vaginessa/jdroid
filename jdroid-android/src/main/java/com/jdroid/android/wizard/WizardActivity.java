package com.jdroid.android.wizard;

import java.util.List;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.view.ButtonBarView;
import com.jdroid.android.view.CustomViewPager;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class WizardActivity extends AbstractFragmentActivity {
	
	private CustomViewPager pager;
	private ButtonBarView buttonBarView;
	
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
		
		buttonBarView = findView(R.id.buttonBar);
		buttonBarView.findViewById(R.id.topDivider).setVisibility(View.VISIBLE);
		
		buttonBarView.setNegativeTextId(getNegativeStringResId());
		buttonBarView.setNegativeOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		});
		buttonBarView.setNegativeDrawableId(0);
		
		buttonBarView.setPositiveOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (isOnFinishStep()) {
					onfinishWizard();
				} else {
					pager.setCurrentItem(pager.getCurrentItem() + 1);
				}
			}
		});
		buttonBarView.setNegativeDrawableId(0);
		
		updateBottomBar();
	}
	
	public abstract List<? extends WizardStep> getWizardSteps();
	
	protected Integer getOffscreenPageLimit() {
		return null;
	};
	
	protected int getNegativeStringResId() {
		return R.string.previous;
	}
	
	protected int getPositiveStringResId() {
		return R.string.next;
	}
	
	protected int getFinishStringResId() {
		return R.string.finish;
	}
	
	public void enableNegativeButton() {
		buttonBarView.getNegativeButton().setEnabled(true);
		pager.setPagingEnabled(true);
	}
	
	public void disableNegativeButton() {
		buttonBarView.getNegativeButton().setEnabled(false);
		pager.setPagingEnabled(false);
	}
	
	public void enablePositiveButton() {
		buttonBarView.getPositiveButton().setEnabled(true);
		pager.setPagingEnabled(true);
	}
	
	public void disablePositiveButton() {
		buttonBarView.getPositiveButton().setEnabled(false);
		pager.setPagingEnabled(false);
	}
	
	public WizardStep getCurrentWizardStep() {
		return getWizardSteps().get(pager.getCurrentItem());
	}
	
	private void updateBottomBar() {
		int position = pager.getCurrentItem();
		if (isOnFinishStep()) {
			buttonBarView.setPositiveTextId(getFinishStringResId());
			buttonBarView.getPositiveButton().setBackgroundResource(R.drawable.finish_background);
		} else {
			buttonBarView.setPositiveTextId(getPositiveStringResId());
			buttonBarView.getPositiveButton().setBackgroundResource(R.drawable.default_item_selector);
		}
		
		buttonBarView.getNegativeButton().setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}
	
	private Boolean isOnFinishStep() {
		return pager.getCurrentItem() == (getWizardSteps().size() - 1);
	}
	
	protected abstract void onfinishWizard();
	
}
