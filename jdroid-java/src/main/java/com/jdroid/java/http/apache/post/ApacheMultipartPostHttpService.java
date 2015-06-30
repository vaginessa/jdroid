package com.jdroid.java.http.apache.post;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.ApacheHttpService;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class ApacheMultipartPostHttpService extends ApachePostHttpService implements MultipartHttpService {
	
	private MultipartEntity multipartEntity = new MultipartEntity();
	
	public ApacheMultipartPostHttpService(HttpClientFactory httpClientFactory, Server server,
										  List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpServiceProcessors);
	}
	
	/**
	 * @see ApachePostHttpService#addEntity(org.apache.http.client.methods.HttpEntityEnclosingRequestBase)
	 */
	@Override
	protected void addEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
		httpEntityEnclosingRequestBase.setEntity(multipartEntity);
	}
	
	/**
	 * @see ApacheHttpService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		// The MultipartEntity will fill the proper content type header. So, we need to avoid the override of it
		if (!name.equals(HttpService.CONTENT_TYPE_HEADER)) {
			super.addHeader(name, value);
		}
	}
	
	/**
	 * @see MultipartHttpService#addPart(java.lang.String, java.io.ByteArrayInputStream,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename) {
		multipartEntity.addPart(name, new ByteArrayInputStreamBody(in, mimeType, filename));
	}
	
	/**
	 * @see MultipartHttpService#addPart(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void addPart(String name, Object value, String mimeType) {
		if (value != null) {
			try {
				multipartEntity.addPart(name, new StringBody(value.toString(), mimeType, Charset.defaultCharset()));
			} catch (UnsupportedEncodingException e) {
				throw new UnexpectedException(e);
			}
		}
	}
	
	/**
	 * @see MultipartHttpService#addJsonPart(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addJsonPart(String name, Object value) {
		addPart(name, MarshallerProvider.get().marshall(value, MarshallerMode.COMPLETE, null), MimeType.JSON);
	}
}
