package com.googleplaypublisher;

import java.io.File;
import java.util.Locale;
import com.jdroid.java.utils.FileUtils;

public class LocaleListing {
	
	private Locale locale;
	private String listingPath;
	
	public LocaleListing(Locale locale, String listingPath) {
		this.locale = locale;
		this.listingPath = listingPath;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public String getTitle() {
		return getDetailsContent("title");
	}
	
	public String getShortDescription() {
		return getDetailsContent("shortDescription");
	}
	
	public String getFullDescription() {
		return getDetailsContent("fullDescription");
	}
	
	public String getRecentChanges() {
		return getDetailsContent("recentChanges");
	}
	
	private String getDetailsContent(String item) {
		File file = new File(listingPath + java.io.File.separator + locale.getLanguage() + java.io.File.separator
				+ "details/" + item + ".txt");
		return FileUtils.toString(file);
	}
}
