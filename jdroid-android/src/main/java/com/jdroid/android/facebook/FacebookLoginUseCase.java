package com.jdroid.android.facebook;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.facebook.model.GraphUser;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

public abstract class FacebookLoginUseCase extends DefaultAbstractUseCase {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(FacebookLoginUseCase.class);
	
	private FacebookConnector facebookConnector;
	private BasicFacebookUserInfo facebookUserInfo;
	private Boolean login = true;
	
	/**
	 * @see com.despegar.commons.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		
		if (login) {
			facebookUserInfo = getFacebookUserInfoFromCache();
			if (facebookUserInfo == null) {
				GraphUser fbUser = facebookConnector.executeMeRequest();
				String accessToken = facebookConnector.getAccessToken();
				
				if (fbUser == null) {
					throw new UnexpectedException("Failed to get GraphUser from Facebook");
				}
				
				facebookUserInfo = new BasicFacebookUserInfo();
				facebookUserInfo.setFirstName(fbUser.getFirstName());
				facebookUserInfo.setLastName(fbUser.getLastName());
				Object email = fbUser.asMap().get("email");
				if (email != null) {
					facebookUserInfo.setEmail(email.toString());
				}
				
				SocialUtils.saveBasicFacebookUserInfo(accessToken, facebookUserInfo);
				
				sendFacebookLogin(fbUser.getId(), accessToken);
				
			} else {
				LOGGER.debug("facebookUserInfo from cache facebookUserInfo= " + facebookUserInfo);
			}
		} else {
			sendFacebookLogout();
		}
	}
	
	protected abstract void sendFacebookLogin(String facebookId, String token);
	
	protected abstract void sendFacebookLogout();
	
	public void setFacebookConnector(FacebookConnector facebookConnector) {
		this.facebookConnector = facebookConnector;
	}
	
	public BasicFacebookUserInfo getFacebookUserInfo() {
		return facebookUserInfo;
	}
	
	private BasicFacebookUserInfo getFacebookUserInfoFromCache() {
		BasicFacebookUserInfo facebookUserInfo = null;
		String accessToken = facebookConnector.getAccessToken();
		if ((accessToken != null) && StringUtils.isNotBlank(accessToken)) {
			String accessTokenHash = AndroidEncryptionUtils.generateShaHash(accessToken);
			LOGGER.debug(" accessTokenHash=" + accessTokenHash);
			String savedAccessTokenHash = SocialUtils.loadFacebookAccessTokenHashFromPreferences();
			if (accessTokenHash.equals(savedAccessTokenHash)) {
				facebookUserInfo = SocialUtils.loadSavedFacebookUserInfo();
			}
		}
		return facebookUserInfo;
	}
	
	public void setLogin(Boolean login) {
		this.login = login;
	}
	
	public Boolean isLogin() {
		return login;
	}
	
}
