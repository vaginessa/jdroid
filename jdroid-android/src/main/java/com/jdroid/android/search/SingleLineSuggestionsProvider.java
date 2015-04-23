package com.jdroid.android.search;

import android.annotation.SuppressLint;
import android.content.SearchRecentSuggestionsProvider;
import com.jdroid.android.utils.AndroidUtils;

/**
 * Declare on your manifest as:
 * 
 * <pre>
 * &lt;provider android:name=".android.common.search.SingleLineSuggestionsProvider"
 * 	android:authorities="[PackageName].SingleLineSuggestionsProvider" />
 * </pre>
 * 
 */
@SuppressLint("Registered")
public class SingleLineSuggestionsProvider extends SearchRecentSuggestionsProvider {
	
	public final static String AUTHORITY = AndroidUtils.getApplicationId() + ".SingleLineSuggestionsProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;
	
	public SingleLineSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
}