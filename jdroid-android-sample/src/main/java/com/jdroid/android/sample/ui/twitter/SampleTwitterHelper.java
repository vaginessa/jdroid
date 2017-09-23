package com.jdroid.android.sample.ui.twitter;

import com.jdroid.android.twitter.DefaultTwitterHelper;
import com.twitter.sdk.android.tweetui.SearchTimeline;

public abstract class SampleTwitterHelper extends DefaultTwitterHelper {

	@Override
	protected SearchTimeline createSearchTimeline() {
		SearchTimeline.Builder searchTimelineBuilder = new SearchTimeline.Builder();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("android");
		searchTimelineBuilder.maxItemsPerRequest(15);
		searchTimelineBuilder.languageCode("en");
		searchTimelineBuilder.query(queryBuilder.toString());
		return searchTimelineBuilder.build();
	}

}
