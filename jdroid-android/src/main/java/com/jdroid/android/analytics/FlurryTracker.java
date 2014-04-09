package com.jdroid.android.analytics;

import java.util.Map;
import android.app.Activity;
import android.util.Log;
import com.flurry.android.FlurryAgent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.ConnectionException;

/**
 * 
 * @author Maxi Rosson
 */
public class FlurryTracker implements AnalyticsTracker {
	
	private static final String FIRST_APPLOAD_SENT = "firstAppLoadSent";
	private static final String FIRST_APPLOAD = "firstAppLoad";
	private static final String CONNECTION_EXCEPTION = "connectionException";
	private static final String URI_HANDLED = "uriHandled";
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAppContext().isFlurryEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#init()
	 */
	@Override
	public void init() {
		if (AbstractApplication.get().getAppContext().isFlurryDebugEnabled()) {
			FlurryAgent.setLogEnabled(true);
			FlurryAgent.setLogLevel(Log.VERBOSE);
		} else {
			FlurryAgent.setLogEnabled(false);
		}
		
		FlurryAgent.setCaptureUncaughtExceptions(false);
		FlurryAgent.setVersionName(AndroidUtils.getVersionName());
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Activity activity, Object data) {
		FlurryAgent.onStartSession(activity, AbstractApplication.get().getAppContext().getFlurryApiKey());
		FlurryAgent.setUserId(AbstractApplication.get().getInstallationId());
		trackFirstAppLoad(data);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(Activity activity) {
		FlurryAgent.onEndSession(activity);
	}
	
	protected void trackFirstAppLoad(Object data) {
		Boolean dataUpdateSent = SharedPreferencesUtils.loadPreferenceAsBoolean(FIRST_APPLOAD_SENT, false);
		if (!dataUpdateSent) {
			String installationSource = SharedPreferencesUtils.loadPreference(AbstractApplication.INSTALLATION_SOURCE);
			if (installationSource != null) {
				Map<String, String> params = Maps.newHashMap();
				params.put("installationSource", installationSource);
				params.put("installedOnSdCard", AndroidUtils.isInstalledOnSdCard().toString());
				params.put("apiLevel", AndroidUtils.getApiLevel().toString());
				params.put("screenDensity", AndroidUtils.getScreenDensity());
				params.put("deviceName", AndroidUtils.getDeviceName());
				params.put("deviceType", AndroidUtils.getDeviceType());
				params.put("smallestWidthDp", AndroidUtils.getSmallestScreenWidthDp().toString());
				Map<String, String> extraParams = getFirstAppLoadExtraParams(data);
				if (extraParams != null) {
					params.putAll(extraParams);
				}
				trackEvent(FIRST_APPLOAD, params);
				SharedPreferencesUtils.savePreference(FIRST_APPLOAD_SENT, true);
			}
		}
	}
	
	protected Map<String, String> getFirstAppLoadExtraParams(Object data) {
		return Maps.newHashMap();
	}
	
	protected void trackEvent(String eventId) {
		trackEvent(eventId, null);
	}
	
	protected void trackEvent(String eventId, Map<String, String> params) {
		if ((params != null) && !params.isEmpty()) {
			FlurryAgent.logEvent(eventId, params);
		} else {
			FlurryAgent.logEvent(eventId);
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(ConnectionException connectionException) {
		Map<String, String> params = Maps.newHashMap();
		params.put("apiLevel", AndroidUtils.getApiLevel().toString());
		params.put("deviceName", AndroidUtils.getDeviceName());
		params.put("httpImplementation", "Apache");
		params.put("errorMessage", connectionException.getMessage());
		
		trackEvent(CONNECTION_EXCEPTION, params);
	}
	
	@Override
	public void trackUriHandled(Boolean handled, String validUri, String invalidUri) {
		Map<String, String> params = Maps.newHashMap();
		params.put("handled", handled.toString());
		params.put("validUri", validUri);
		params.put("invalidUri", invalidUri);
		trackEvent(URI_HANDLED, params);
	}
	
	protected String getDataRange(long dataSize) {
		int number = 2;
		int power = 1;
		while (dataSize > Math.pow(number, power)) {
			power++;
		}
		StringBuilder builder = new StringBuilder();
		builder.append((int)Math.pow(number, power - 1));
		builder.append('-');
		builder.append((int)Math.pow(number, power));
		
		return builder.toString();
	}
	
}
