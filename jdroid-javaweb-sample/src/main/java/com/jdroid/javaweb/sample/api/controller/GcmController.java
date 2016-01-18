package com.jdroid.javaweb.sample.api.controller;

import com.jdroid.java.date.DateUtils;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.api.AbstractController;
import com.jdroid.javaweb.google.gcm.GcmMessage;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceParser;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/gcm")
public class GcmController extends AbstractController {
	
	@Autowired
	private PushService pushService;

	@Autowired
	private DeviceRepository deviceRepository;
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public void send(@RequestParam(required = false) String googleServerApiKey, @RequestParam(required = false) String registrationToken,
					 @RequestParam String messageKeyExtraName,
					 @RequestParam String messageKey, @RequestParam(required = false) String collapseKey,
					 @RequestParam(required = false) String highPriority, @RequestParam(required = false) String delayWhileIdle,
					 @RequestParam(required = false) Integer timeToLive, @RequestParam(required = false) String timestampEnabled,
					 @RequestParam(required = false) String params) {

		GcmMessage pushMessage = new GcmMessage(messageKeyExtraName, messageKey);
		pushMessage.setGoogleServerApiKey(googleServerApiKey);
		if (StringUtils.isNotEmpty(registrationToken)) {
			pushMessage.setTo(registrationToken);
		} else {
			List<Device> devices = deviceRepository.getAll();
			for(Device device : devices) {
				if (device.getRegistrationToken() != null) {
					pushMessage.addRegistrationId(device.getRegistrationToken());
				}
			}
		}

		pushMessage.setCollapseKey(StringUtils.isNotEmpty(collapseKey) ? collapseKey : null);
		if (highPriority != null && highPriority.equalsIgnoreCase("true")) {
			pushMessage.markAsHighPriority();
		}
		pushMessage.setDelayWhileIdle(delayWhileIdle != null && delayWhileIdle.equalsIgnoreCase("true"));
		pushMessage.setTimeToLive(timeToLive);
		if (timestampEnabled != null && timestampEnabled.equalsIgnoreCase("true")) {
			pushMessage.addParameter("timestamp", DateUtils.nowMillis());
		}

		if (params != null) {
			for (String param : StringUtils.splitToCollectionWithCommaSeparator(params.replace("[", "").replace("]", ""))) {
				String[] vec = param.split("\\|");
				pushMessage.addParameter(vec[0], vec[1]);
			}
		}
		pushService.send(pushMessage);
	}

	@RequestMapping(value = "/device", method = RequestMethod.GET, produces = MimeType.JSON_UTF8)
	@ResponseBody
	public String getAllDevices() {
		List<Device> devices = deviceRepository.getAll();
//		InstanceIdApiService instanceIdApiService = new InstanceIdApiService();
//		for(Device device : devices) {
//			instanceIdApiService.verify(device.getRegistrationToken(), Application.get().getAppContext().getGoogleServerApiKey());
//		}
		return marshall(devices);
	}

	@RequestMapping(value = "/device", method = RequestMethod.POST)
	public void addDevice(@RequestHeader(value = "instanceId") String instanceId, @RequestHeader(value = "User-Agent") String userAgent,
						  @RequestHeader(value="Accept-Language") String acceptLanguage, @RequestBody String deviceJSON, @RequestParam Boolean updateLastActiveTimestamp) {
		DeviceParser parser = new DeviceParser(instanceId, userAgent, acceptLanguage);
		Device device = (Device)parser.parse(deviceJSON);
		pushService.addDevice(device, updateLastActiveTimestamp);
	}

	@RequestMapping(value = "/device", method = RequestMethod.DELETE)
	public void removeDevice(@RequestHeader(value = "User-Agent") String userAgent, @RequestHeader(value = "instanceId") String instanceId) {
		pushService.removeDevice(instanceId, DeviceType.find(userAgent));
	}
}