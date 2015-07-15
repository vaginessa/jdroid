package com.jdroid.java.http.post;

import com.jdroid.java.http.HttpService;

public interface BodyEnclosingHttpService extends HttpService {
	
	public void setBody(String body);
	
}
