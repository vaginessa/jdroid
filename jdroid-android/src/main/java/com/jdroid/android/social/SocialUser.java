package com.jdroid.android.social;

import com.jdroid.android.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUser extends Entity {
	
	private String socialId;
	private AccountType socialNetwork;
	private String firstName;
	private String lastName;
	private String imageURL;
	
	public SocialUser(Long id, String socialId, AccountType socialNetwork, String firstName, String lastName,
			String imageURL) {
		super(id);
		this.socialId = socialId;
		this.socialNetwork = socialNetwork;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imageURL = imageURL;
	}
	
	public String getFullname() {
		return firstName + " " + lastName;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	public String getImageUrl() {
		return imageURL;
	}
	
	/**
	 * @return the socialNetwork
	 */
	public AccountType getSocialNetwork() {
		return socialNetwork;
	}
	
	/**
	 * @return the socialId
	 */
	public String getSocialId() {
		return socialId;
	}
}
