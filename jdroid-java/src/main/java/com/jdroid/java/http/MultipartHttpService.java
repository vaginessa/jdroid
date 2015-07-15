package com.jdroid.java.http;

import java.io.ByteArrayInputStream;
import com.jdroid.java.http.post.BodyEnclosingHttpService;

public interface MultipartHttpService extends BodyEnclosingHttpService {
	
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename);
	
	public void addPart(String name, Object value, String mimeType);
	
	public void addJsonPart(String name, Object value);
	
}
