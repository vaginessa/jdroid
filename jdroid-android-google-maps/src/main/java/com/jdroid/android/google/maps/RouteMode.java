package com.jdroid.android.google.maps;

import com.jdroid.android.dialog.ChoiceItem;

public enum RouteMode implements ChoiceItem {
	
	WALKING(R.color.walkingColor, R.string.walking, "walking"),
	DRIVING(R.color.drivingColor, R.string.driving, "driving");
	
	private int colorId;
	private int resourceTitleId;
	private String argName;
	
	private RouteMode(int colorId, int resourceTitleId, String argName) {
		this.colorId = colorId;
		this.resourceTitleId = resourceTitleId;
		this.argName = argName;
	}
	
	/**
	 * @return the colorId
	 */
	public int getColorId() {
		return colorId;
	}
	
	/**
	 * @return the argName
	 */
	public String getArgName() {
		return argName;
	}
	
	/**
	 * @see com.jdroid.android.dialog.ChoiceItem#getResourceTitleId()
	 */
	@Override
	public int getResourceTitleId() {
		return resourceTitleId;
	}
}