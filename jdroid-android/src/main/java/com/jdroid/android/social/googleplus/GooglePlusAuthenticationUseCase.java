package com.jdroid.android.social.googleplus;

import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.usecase.DefaultAbstractUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class GooglePlusAuthenticationUseCase extends DefaultAbstractUseCase {
	
	private static final long serialVersionUID = -6947436509726048083L;
	
	private Boolean loginMode = true;
	private String account;
	private Person person;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (loginMode) {
			onConnectToGooglePlus(person, account);
		} else {
			onDisconnectFromGooglePlus();
		}
	}
	
	protected abstract void onConnectToGooglePlus(Person person, String account);
	
	protected abstract void onDisconnectFromGooglePlus();
	
	public String getProfilePictureUrl(int size) {
		String profilePictureUrl = person.hasImage() ? person.getImage().getUrl() : null;
		if (profilePictureUrl != null) {
			profilePictureUrl = profilePictureUrl + "&sz=" + size;
		}
		return profilePictureUrl;
	}
	
	public String getCoverPictureUrl() {
		return person.hasCover() && person.getCover().hasCoverPhoto() ? person.getCover().getCoverPhoto().getUrl()
				: null;
	}
	
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
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getAccount() {
		return account;
	}
}
