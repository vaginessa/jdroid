package com.jdroid.java.http.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.jdroid.java.parser.Parser;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.FileUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class CacheParser implements Parser {
	
	private Parser parser;
	private File cacheFile;
	
	public CacheParser(Parser parser, File cacheFile) {
		this.parser = parser;
		this.cacheFile = cacheFile;
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.io.InputStream)
	 */
	@SuppressWarnings("resource")
	@Override
	public Object parse(InputStream inputStream) {
		
		final InputStream inputStreamCopy = FileUtils.copy(inputStream);
		Object object = parser.parse(inputStreamCopy);
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					inputStreamCopy.reset();
				} catch (IOException e) {
					// Do Nothing
				}
				FileUtils.copyStream(inputStreamCopy, cacheFile);
			}
		});
		return object;
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.lang.String)
	 */
	@Override
	public Object parse(String input) {
		return null;
	}
	
}
