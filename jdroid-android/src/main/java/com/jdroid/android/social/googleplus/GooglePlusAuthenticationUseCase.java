package com.jdroid.android.social.googleplus;

import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.usecase.DefaultAbstractUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class GooglePlusAuthenticationUseCase extends DefaultAbstractUseCase {
	
	private Boolean loginMode = true;
	private Person person;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (loginMode) {
			onConnectToGooglePlus(person.getId());
		} else {
			onDisconnectFromGooglePlus();
		}
	}
	
	protected abstract void onConnectToGooglePlus(String googleUserId);
	
	protected abstract void onDisconnectFromGooglePlus();
	
	/**
	 * @param loginMode the loginMode to set
	 */
	public void setLoginMode(Boolean loginMode) {
		this.loginMode = loginMode;
	}
	
	public Boolean isLoginMode() {
		return loginMode;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Person getPerson() {
		return person;
	}
}
