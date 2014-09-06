package com.jdroid.java.http.apache.post;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.message.BasicNameValuePair;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.utils.EncodingUtils;

public class ApacheFormHttpPostWebService extends ApacheHttpPostWebService {
	
	public ApacheFormHttpPostWebService(HttpClientFactory httpClientFactory, Server server, List<Object> urlSegments,
			List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.post.ApacheHttpPostWebService#addEntity(org.apache.http.client.methods.HttpEntityEnclosingRequestBase)
	 */
	@Override
	protected void addEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
		try {
			List<NameValuePair> nameValuePairs = Lists.newArrayList();
			for (Entry<String, String> entry : getQueryParameters().entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpEntityEnclosingRequestBase.setEntity(new UrlEncodedFormEntity(nameValuePairs, EncodingUtils.UTF8));
		} catch (UnsupportedEncodingException e) {
			throw new UnexpectedException(e);
		}
	}
}
