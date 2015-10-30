package com.jdroid.android.instanceid;

import com.google.android.gms.iid.InstanceID;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.UUID;

public class InstanceIdHelper {

	private final static Logger LOGGER = LoggerUtils.getLogger(InstanceIdHelper.class);

	private static final String INSTANCE_ID = "instanceId";
	private static final String ANONYMOUS_INSTANCE_ID = "anonymousInstanceId";

	private static String instanceId;
	private static String anonymousInstanceId;

	public static synchronized String getInstanceId() {
		if (instanceId == null) {
			instanceId = SharedPreferencesHelper.get().loadPreference(INSTANCE_ID);
			if (instanceId == null) {
				instanceId = InstanceID.getInstance(AbstractApplication.get()).getId();
				SharedPreferencesHelper.get().savePreference(INSTANCE_ID, instanceId);
			}
			LOGGER.debug("Instance id: " + instanceId);
		}
		if (instanceId != null) {
			return instanceId;
		} else {
			if (anonymousInstanceId == null) {
				anonymousInstanceId = SharedPreferencesHelper.get().loadPreference(ANONYMOUS_INSTANCE_ID);
				if (anonymousInstanceId == null) {
					anonymousInstanceId = "anonymous" + UUID.randomUUID();
					SharedPreferencesHelper.get().savePreference(ANONYMOUS_INSTANCE_ID, anonymousInstanceId);
				}
				LOGGER.debug("Anonymous Instance id: " + anonymousInstanceId);
			}
			return anonymousInstanceId;
		}
	}

	public static void clearInstanceId() {
		instanceId = null;
		SharedPreferencesHelper.get().removePreferences(INSTANCE_ID);
	}

}
