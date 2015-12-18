package com.jdroid.javaweb.api;

import com.google.common.collect.Maps;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.date.DateUtils;
import com.jdroid.javaweb.context.Application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

public class AdminController extends AbstractController {

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String getAppInfo() {

		Map<String, Object> infoMap = Maps.newLinkedHashMap();
		infoMap.put("App Name", Application.get().getAppContext().getAppName());
		infoMap.put("App Version", Application.get().getAppContext().getAppVersion());
		infoMap.put("Build Type", Application.get().getAppContext().getBuildType());
		infoMap.put("Build Time", Application.get().getAppContext().getBuildTime());
		infoMap.put("Git Branch", Application.get().getGitContext().getBranch());
		infoMap.put("Git Sha", Application.get().getGitContext().getSha());
		infoMap.put("Http Mock Enabled", Application.get().getAppContext().isHttpMockEnabled());
		infoMap.put("Http Mock Sleep Duration", Application.get().getAppContext().getHttpMockSleepDuration());
		infoMap.put("Default Charset", Charset.defaultCharset());
		infoMap.put("File Encoding", System.getProperty("file.encoding"));

		infoMap.put("Time Zone", TimeZone.getDefault().getID());
		infoMap.put("Current Time", DateUtils.now());

		// Twitter
		infoMap.put("Twitter Enabled", Application.get().getAppContext().isTwitterEnabled());
		infoMap.put("Twitter Oauth Consumer Key", Application.get().getAppContext().getTwitterOAuthConsumerKey());
		infoMap.put("Twitter Oauth Consumer Secret", Application.get().getAppContext().getTwitterOAuthConsumerSecret());
		infoMap.put("Twitter Oauth Access Token", Application.get().getAppContext().getTwitterOAuthAccessToken());
		infoMap.put("Twitter Oauth Access Token Secret", Application.get().getAppContext().getTwitterOAuthAccessTokenSecret());

		// RollBar
		infoMap.put("RollBar Enabled", Application.get().getAppContext().isRollBarEnabled());
		infoMap.put("RollBar Access Token", Application.get().getAppContext().getRollBarAccessToken());

		// Google
		infoMap.put("Google Server API Key", Application.get().getAppContext().getGoogleServerApiKey());

		infoMap.putAll(getCustomInfoMap());

		StringBuilder builder = new StringBuilder();
		for (Entry<String, Object> entry : infoMap.entrySet()) {
			if (entry.getValue() != null) {
				builder.append("\n");
				builder.append(entry.getKey());
				builder.append(": ");
				builder.append(entry.getValue());
			}
		}


		return builder.toString();
	}

	protected Map<String, Object> getCustomInfoMap() {
		return Maps.newHashMap();
	}
}
