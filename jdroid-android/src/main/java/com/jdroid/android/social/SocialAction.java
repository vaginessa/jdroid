package com.jdroid.android.social;

/**
 * 
 * @author Maxi Rosson
 */
public enum SocialAction {
	
	LIKE("like"),
	SHARE("share"),
	PLUS_ONE("+1"),
	PLUS_ONE_UNDO("-1");
	
	private String name;
	
	private SocialAction(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
