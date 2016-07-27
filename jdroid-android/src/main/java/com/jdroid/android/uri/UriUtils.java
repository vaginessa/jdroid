package com.jdroid.android.uri;

import android.net.Uri;

import com.jdroid.java.utils.RandomUtils;
import com.jdroid.java.utils.StringUtils;

public class UriUtils {

	public static String RANDOM_PARAMETER = "rnd";

	public static Uri addRandomParam(Uri uri) {
		String uriString = uri.toString();
		StringBuilder builder = new StringBuilder(uriString);
		if (uri.getPathSegments().isEmpty() && StringUtils.isEmpty(uri.getQuery()) && !uriString.endsWith("/")) {
			builder.append("/");
		}
		if (StringUtils.isEmpty(uri.getQuery())) {
			builder.append("?");
		} else{
			builder.append("&");
		}
		builder.append(RANDOM_PARAMETER);
		builder.append("=");
		builder.append(RandomUtils.getInt());
		return Uri.parse(builder.toString());
	}
}
