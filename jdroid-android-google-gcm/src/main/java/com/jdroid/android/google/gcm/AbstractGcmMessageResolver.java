package com.jdroid.android.google.gcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.google.gcm.notification.NotificationGcmMessage;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.NumberUtils;

import org.slf4j.Logger;

import java.util.List;

public abstract class AbstractGcmMessageResolver implements GcmMessageResolver {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractGcmMessageResolver.class);
	
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	private static final String USER_ID_KEY = "userIdKey";
	private static final String MIN_APP_VERSION_CODE_KEY = "minAppVersionCode";

	private List<GcmMessage> gcmMessages;
	
	public AbstractGcmMessageResolver(List<GcmMessage> gcmMessages) {
		this.gcmMessages = gcmMessages;
		if (includeNotificationGcmMessage()) {
			this.gcmMessages.add(createNotificationGcmMessage());
		}
	}
	
	public AbstractGcmMessageResolver(GcmMessage... gcmMessages) {
		this(Lists.newArrayList(gcmMessages));
	}

	protected NotificationGcmMessage createNotificationGcmMessage() {
		return new NotificationGcmMessage();
	}

	protected Boolean includeNotificationGcmMessage() {
		return true;
	}

	@Override
	public GcmMessage resolve(RemoteMessage remoteMessage) {
		String messageKey = remoteMessage.getData().get(getMessageKeyExtraName());
		LOGGER.debug("FCM message received. / Message Key: " + messageKey);
		Long minAppVersionCode =  NumberUtils.getLong(remoteMessage.getData().get(MIN_APP_VERSION_CODE_KEY), 0L);
		if (AppUtils.getVersionCode() >= minAppVersionCode) {
			for (GcmMessage each : gcmMessages) {
				if (each.getMessageKey().equalsIgnoreCase(messageKey)) {

					Long userId = NumberUtils.getLong(remoteMessage.getData().get(USER_ID_KEY));

					// We should ignore messages received for previously logged users
					if ((userId != null) && (!SecurityContext.get().isAuthenticated() || !SecurityContext.get().getUser().getId().equals(userId))) {
						LOGGER.warn("The FCM message is ignored because it was sent to another user: " + userId);
						onNotAuthenticatedUser(userId);
						return null;
					}
					return each;
				}
			}
			AbstractApplication.get().getExceptionHandler().logWarningException("The FCM message key [" + messageKey + "] is unknown");
		} else {
			LOGGER.debug("Ignoring FCM message [" + messageKey + "]. minAppVersionCode: " + minAppVersionCode);
		}
		return null;
	}
	
	protected String getMessageKeyExtraName() {
		return MESSAGE_KEY_EXTRA;
	}
	
	protected abstract void onNotAuthenticatedUser(Long userId);
}
