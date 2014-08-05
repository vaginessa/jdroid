package com.jdroid.sample.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.push.gcm.AbstractGcmMessage;

@Controller
@RequestMapping("/gcm")
public class GcmController {
	
	@Autowired
	private PushService pushService;
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public void send(@RequestParam String registrationId, final @RequestParam String messageKeyExtraName,
			final @RequestParam String messageKey, @RequestParam String params) {
		
		PushMessage pushMessage = new AbstractGcmMessage() {
			
			@Override
			protected String getMessageKeyExtraName() {
				return messageKeyExtraName;
			}
			
			@Override
			public String getMessageKey() {
				return messageKey;
			}
		};
		
		if (params != null) {
			for (String param : StringUtils.splitToCollection(params.replace("[", "").replace("]", ""))) {
				String[] vec = param.split("\\|");
				pushMessage.addParameter(vec[0], vec[1]);
			}
		}
		Device device = new Device(null, registrationId, DeviceType.ANDROID);
		
		pushService.send(pushMessage, device);
	}
}