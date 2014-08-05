package com.jdroid.android;

import java.io.Serializable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public interface ActionItem extends Serializable {
	
	public String getName();
	
	public Integer getIconResource();
	
	public Integer getNameResource();
	
	public Integer getDescriptionResource();
	
	public Fragment createFragment(Object args);
	
	public void startActivity(FragmentActivity fragmentActivity);
	
	public Boolean matchesActivity(FragmentActivity fragmentActivity);
}
