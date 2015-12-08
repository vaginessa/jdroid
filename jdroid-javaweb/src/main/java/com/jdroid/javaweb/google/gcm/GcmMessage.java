package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushMessage;

import java.util.List;
import java.util.Map;

public class GcmMessage implements PushMessage {

	private String googleServerApiKey;

	// Required. This parameter specifies the recipient of a message. The value must be a registration token, notification key, or topic
	private String to;

	// This parameter specifies a list of devices (registration tokens, or IDs) receiving a multicast message.
	// It must contain at least 1 and at most 1000 registration tokens.
	// Use this parameter only for multicast messaging, not for single recipients.
	// Multicast messages (sending to more than 1 registration tokens) are allowed using HTTP JSON format only.
	private List<String> registrationIds;

	// Optional. This parameter identifies a group of messages (e.g., with collapse_key: "Updates Available") that can be collapsed,
	// so that only the last message gets sent when delivery can be resumed.
	// This is intended to avoid sending too many of the same messages when the device comes back online or becomes active (see delay_while_idle).
	// Note that there is no guarantee of the order in which messages get sent.
	// Note: A maximum of 4 different collapse keys is allowed at any given time.
	// This means a GCM connection server can simultaneously store 4 different send-to-sync messages per client app.
	// If you exceed this number, there is no guarantee which 4 collapse keys the GCM connection server will keep.
	private String collapseKey;

	// Sets the priority of the message. Valid values are "normal" and "high." On iOS, these correspond to APNs priority 5 and 10.
	// By default, messages are sent with normal priority. Normal priority optimizes the client app's battery consumption,
	// and should be used unless immediate delivery is required. For messages with normal priority, the app may receive the message with unspecified delay.
	// When a message is sent with high priority, it is sent immediately, and the app can wake a sleeping device and open a network connection to your server.
	private GcmMessagePriority priority = GcmMessagePriority.NORMAL;

	// Optional. When this parameter is set to true, it indicates that the message should not be sent until
	// the device becomes active. The default value is false.
	private Boolean delayWhileIdle = false;

	// Optional. This parameter specifies how long (in seconds) the message should be kept in GCM storage if the device is offline.
	// The maximum time to live supported is 4 weeks. The default value is 4 weeks.
	private Integer timeToLive;

	// Optional. This parameter specifies the custom key-value pairs of the message's payload.
	// For example, with data:{"score":"3x1"}:
	// - On Android, this would result in an intent extra named score with the string value 3x1.
	// - On iOS, if the message is sent via APNS, it represents the custom data fields.
	//   If it is sent via GCM connection server, it would be represented as key value dictionary in
	//   AppDelegate application:didReceiveRemoteNotification:.
	// The key should not be a reserved word ("from" or any word starting with "google" or "gcm").
	// Values in string types are recommended. You have to convert values in objects or other non-string
	// data types (e.g., integers or booleans) to string.
	private Map<String, String> data = Maps.newHashMap();

	public GcmMessage() {
		// Do nothing
	}

	public GcmMessage(String messageKey) {
		this("messageKey", messageKey);
	}

	public GcmMessage(String messageKeyExtraName, String messageKey) {
		addParameter(messageKeyExtraName, messageKey);
	}

	@Override
	public DeviceType getDeviceType() {
		return DeviceType.ANDROID;
	}

	@Override
	public void addParameter(String key, String value) {
		if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
			data.put(key, value);
		}
	}

	@Override
	public void addParameter(String key, Boolean value) {
		if (StringUtils.isNotEmpty(key) && value != null) {
			data.put(key, value.toString());
		}
	}

	@Override
	public void addParameter(String key, Integer value) {
		if (StringUtils.isNotEmpty(key) && value != null) {
			data.put(key, value.toString());
		}
	}

	@Override
	public void addParameter(String key, Long value) {
		if (StringUtils.isNotEmpty(key) && value != null) {
			data.put(key, value.toString());
		}
	}

	@Override
	public Map<String, String> getParameters() {
		return data;
	}
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<String> getRegistrationIds() {
		return registrationIds;
	}

	public void addRegistrationId(String registrationId) {
		if (registrationIds == null) {
			registrationIds = Lists.newArrayList();
		}
		registrationIds.add(registrationId);
	}

	public void setRegistrationIds(List<String> registrationIds) {
		this.registrationIds = registrationIds;
	}

	public String getCollapseKey() {
		return collapseKey;
	}

	public void setCollapseKey(String collapseKey) {
		this.collapseKey = collapseKey;
	}

	public GcmMessagePriority getPriority() {
		return priority;
	}

	public void setPriority(GcmMessagePriority priority) {
		this.priority = priority;
	}

	public void markAsHighPriority() {
		this.priority = GcmMessagePriority.HIGH;
	}

	public Boolean isDelayWhileIdle() {
		return delayWhileIdle;
	}

	public void setDelayWhileIdle(Boolean delayWhileIdle) {
		this.delayWhileIdle = delayWhileIdle;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	public String getGoogleServerApiKey() {
		return googleServerApiKey;
	}

	public void setGoogleServerApiKey(String googleServerApiKey) {
		this.googleServerApiKey = googleServerApiKey;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("GcmMessage{");
		sb.append("googleServerApiKey='").append(googleServerApiKey).append('\'');
		sb.append(", to='").append(to).append('\'');
		sb.append(", registrationIds=").append(registrationIds);
		sb.append(", collapseKey='").append(collapseKey).append('\'');
		sb.append(", priority=").append(priority);
		sb.append(", delayWhileIdle=").append(delayWhileIdle);
		sb.append(", timeToLive=").append(timeToLive);
		sb.append(", data=").append(data);
		sb.append('}');
		return sb.toString();
	}
}
