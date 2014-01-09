package com.jdroid.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.R;

/**
 * 
 * @author Maxi Rosson
 */
public class ViewBuilder {
	
	public static View buildSectionTitle(Context context, int titleId, Object... args) {
		return buildSectionTitle(context, context.getString(titleId, args));
	}
	
	public static View buildSectionTitle(Context context, String title) {
		View sectionTitle = LayoutInflater.from(context).inflate(R.layout.section_title, null);
		TextView textView = (TextView)sectionTitle.findViewById(R.id.title);
		textView.setText(title);
		return sectionTitle;
	}
	
}
