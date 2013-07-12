package com.jdroid.android.facebook;

import org.apache.commons.lang.StringUtils;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionState;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;

public class SocialUtils {
	
	private static final String PREFS_FACEBOOK_ACCESS_TOKEN = "facebook.accessToken";
	private static final String PREFS_FIRST_NAME = "facebook.firstName";
	private static final String PREFS_LAST_NAME = "facebook.lastName";
	private static final String PREFS_USER_EMAIL = "facebook.userEmail";
	
	public static String loadFacebookAccessTokenHashFromPreferences() {
		return SharedPreferencesUtils.loadPreference(PREFS_FACEBOOK_ACCESS_TOKEN);
	}
	
	public static BasicFacebookUserInfo loadSavedFacebookUserInfo() {
		BasicFacebookUserInfo facebookUserInfo = null;
		String firstName = SharedPreferencesUtils.loadPreference(PREFS_FIRST_NAME);
		String lastName = SharedPreferencesUtils.loadPreference(PREFS_LAST_NAME);
		String email = SharedPreferencesUtils.loadPreference(PREFS_USER_EMAIL);
		
		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(email)) {
			facebookUserInfo = new BasicFacebookUserInfo();
			facebookUserInfo.setFirstName(firstName);
			facebookUserInfo.setEmail(email);
			facebookUserInfo.setLastName(lastName);
		}
		return facebookUserInfo;
	}
	
	public static void saveBasicFacebookUserInfo(String accessToken, BasicFacebookUserInfo facebookUserInfo) {
		Editor editor = SharedPreferencesUtils.getEditor();
		editor.putString(PREFS_FACEBOOK_ACCESS_TOKEN, AndroidEncryptionUtils.generateShaHash(accessToken));
		editor.putString(PREFS_FIRST_NAME, facebookUserInfo.getFirstName());
		editor.putString(PREFS_LAST_NAME, facebookUserInfo.getLasttName());
		editor.putString(PREFS_USER_EMAIL, facebookUserInfo.getEmail());
		editor.commit();
	}
	
	public static Session openFacebookActiveSession(Context context, String applicationId) {
		Builder builder = new Builder(context);
		builder.setApplicationId(applicationId);
		Session session = builder.build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())) {
			Session.setActiveSession(session);
			session.openForRead(null);
			return session;
		}
		return null;
	}
}
