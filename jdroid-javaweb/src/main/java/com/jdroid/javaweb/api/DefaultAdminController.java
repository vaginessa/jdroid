package com.jdroid.javaweb.api;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.collect.Maps;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.http.MimeType;
import com.jdroid.javaweb.context.DefaultApplication;

/**
 * @author Maxi Rosson
 */
public class DefaultAdminController {
	
	@RequestMapping(value = "/info/{key}", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String getAppInfo(@PathVariable String key) {
		
		StringBuilder builder = new StringBuilder();
		if (key.equals(getPrivateKey())) {
			Map<String, Object> infoMap = Maps.newLinkedHashMap();
			infoMap.put("App Name", DefaultApplication.get().getDefaultApplicationContext().getAppName());
			infoMap.put("App Version", DefaultApplication.get().getDefaultApplicationContext().getAppVersion());
			infoMap.put("Commit Id", GitContext.get().getCommitId());
			infoMap.put("Commit Time", GitContext.get().getCommitTime());
			infoMap.put("Commit Build Time", GitContext.get().getBuildTime());
			infoMap.put("Http Mock Enabled",
				DefaultApplication.get().getDefaultApplicationContext().isHttpMockEnabled());
			infoMap.put("Http Mock Sleep Duration",
				DefaultApplication.get().getDefaultApplicationContext().getHttpMockSleepDuration());
			infoMap.put("Default Charset", Charset.defaultCharset());
			infoMap.put("File Encoding", System.getProperty("file.encoding"));
			
			infoMap.putAll(getCustomInfoMap());
			
			for (Entry<String, Object> entry : infoMap.entrySet()) {
				builder.append("\n");
				builder.append(entry.getKey());
				builder.append(": ");
				builder.append(entry.getValue());
			}
		}
		return builder.toString();
	}
	
	protected String getPrivateKey() {
		return "private1234";
	}
	
	protected Map<String, Object> getCustomInfoMap() {
		return Maps.newHashMap();
	}
}
