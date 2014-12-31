package com.jdroid.googleplaypublisher;

import java.util.List;
import java.util.Locale;
import com.google.api.client.util.Lists;
import com.jdroid.java.utils.StringUtils;

public class Publisher {
	
	public static void main(String[] args) {
		
		AppContext appContext = new AppContext(args[0]);
		
		GooglePlayPublisher.listApks(appContext);
		
		List<LocaleListing> localeListings = Lists.newArrayList();
		for (String each : StringUtils.splitToCollection(appContext.getLocales())) {
			String[] split = each.split("-");
			String language = split[0];
			String country = "";
			if (split.length > 1) {
				country = split[1];
			}
			localeListings.add(new LocaleListing(new Locale(language, country), appContext.getListingPath()));
		}
		GooglePlayPublisher.updateListings(appContext, localeListings);
		// GooglePlayPublisher.updateApk(appContext, appContext.getApkPath(), appContext.getTrackType(),
		// localeListings);
	}
	
}
