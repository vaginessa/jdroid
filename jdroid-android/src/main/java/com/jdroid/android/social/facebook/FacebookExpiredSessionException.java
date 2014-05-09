package com.jdroid.android.social.facebook;

import com.jdroid.java.exception.AbstractException;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookExpiredSessionException extends AbstractException {
	
	private static final long serialVersionUID = -1347248771940116950L;
	
	public FacebookExpiredSessionException(String message) {
		super(message);
	}
	
}
