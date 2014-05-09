package com.jdroid.java.mail;

import com.jdroid.java.exception.AbstractException;

/**
 * 
 * @author Maxi Rosson
 */
public class MailException extends AbstractException {
	
	private static final long serialVersionUID = 7343781404856218086L;
	
	/**
	 * @param throwable
	 */
	public MailException(Throwable throwable) {
		super(throwable);
	}
	
}
