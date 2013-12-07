package com.jdroid.android.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class EmailAutoCompleteTextView extends AutoCompleteTextView {
	
	public EmailAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public EmailAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public EmailAutoCompleteTextView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_dropdown_item_1line, AndroidUtils.getAccountsEmails());
		setAdapter(adapter);
		setThreshold(1);
		setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}
	
}
