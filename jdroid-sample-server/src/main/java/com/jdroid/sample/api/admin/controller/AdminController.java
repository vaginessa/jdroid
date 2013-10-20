package com.jdroid.sample.api.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.http.MimeType;
import com.jdroid.javaweb.context.DefaultApplication;

/**
 * @author Maxi Rosson
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String getAppInfo() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("App Name: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppName());
		builder.append("\nApp Version: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppVersion());
		builder.append("\nApp URL: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getAppURL());
		builder.append("\nCommit Id: ");
		builder.append(GitContext.get().getCommitId());
		builder.append("\nCommit Time: ");
		builder.append(GitContext.get().getCommitTime());
		builder.append("\nCommit Build Time: ");
		builder.append(GitContext.get().getBuildTime());
		builder.append("\nHttp Mock Enabled: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().isHttpMockEnabled());
		builder.append("\nHttp Mock Sleep Duration: ");
		builder.append(DefaultApplication.get().getDefaultApplicationContext().getHttpMockSleepDuration());
		return builder.toString();
	}
}
