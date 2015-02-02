package com.jdroid.javaweb.api;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.collect.Maps;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.http.MimeType;
import com.jdroid.javaweb.context.Application;

public class AdminController extends AbstractController {
	
	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String getAppInfo() {
		
		Map<String, Object> infoMap = Maps.newLinkedHashMap();
		infoMap.put("App Name", Application.get().getAppContext().getAppName());
		infoMap.put("App Version", Application.get().getAppContext().getAppVersion());
		infoMap.put("Git Branch", GitContext.get().getBranch());
		infoMap.put("Git Commit Id", GitContext.get().getCommitId());
		infoMap.put("Git Commit Time", GitContext.get().getCommitTime());
		infoMap.put("Git Commit Build Time", GitContext.get().getBuildTime());
		infoMap.put("Http Mock Enabled", Application.get().getAppContext().isHttpMockEnabled());
		infoMap.put("Http Mock Sleep Duration", Application.get().getAppContext().getHttpMockSleepDuration());
		infoMap.put("Default Charset", Charset.defaultCharset());
		infoMap.put("File Encoding", System.getProperty("file.encoding"));
		infoMap.put("Time Zone", TimeZone.getDefault().getID());
		
		infoMap.putAll(getCustomInfoMap());
		
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Object> entry : infoMap.entrySet()) {
			builder.append("\n");
			builder.append(entry.getKey());
			builder.append(": ");
			builder.append(entry.getValue());
		}
		return builder.toString();
	}
	
	protected Map<String, Object> getCustomInfoMap() {
		return Maps.newHashMap();
	}
}
