package com.jdroid.android.uri;

import java.util.ArrayList;
import java.util.List;
import android.net.Uri;

/**
 * This matcher check if the beginning of the given path matches with some of the configured path prefixes.
 * 
 */
public class PathPrefixMatcher {
	
	private List<UriHandler<?>> handlers;
	private UriHandler<?> noMatchObject;
	
	public PathPrefixMatcher() {
		handlers = new ArrayList<UriHandler<?>>();
	}
	
	/**
	 * Allows define an object to be returned when there is not match.
	 * 
	 * @param object to be returned when there is not match.
	 */
	public void setNoMatchObject(UriHandler<?> object) {
		noMatchObject = object;
	}
	
	/**
	 * Determine i given {@link UriHandler} is the no match object returned in case of no match.
	 * 
	 * @param object
	 * @return true if it is the no match object.
	 */
	public boolean isNoMatchObject(UriHandler<?> object) {
		return object == noMatchObject;
	}
	
	/**
	 * Add a uri handler for some path prefix.
	 * 
	 * @param uriHandler uriHandler for some path prefix.
	 */
	public void addUriHandler(UriHandler<?> uriHandler) {
		handlers.add(uriHandler);
	}
	
	/**
	 * Checks if uri path segments matches with some of configured path prefixes.
	 * 
	 * @param uri the uri to evaluate.
	 * @return the path uri handler or the no match hander if there is one.
	 */
	public UriHandler<?> match(Uri uri) {
		List<String> pathSegments = uri.getPathSegments();
		
		for (UriHandler<?> uriHandler : handlers) {
			String[] pathPrefixes = uriHandler.getPathPrefixes();
			for (String pathPrefix : pathPrefixes) {
				String[] prefixSegments = pathPrefix.split("/");
				if (pathSegments.size() < prefixSegments.length) {
					continue;
				}
				
				boolean match = true;
				for (int i = 0; i < prefixSegments.length; i++) {
					if (!prefixSegments[i].equals(pathSegments.get(i))) {
						match = false;
						break;
					}
				}
				
				if (match) {
					return uriHandler;
				}
			}
		}
		return noMatchObject;
	}
}
