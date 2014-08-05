package com.jdroid.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.jdroid.android.utils.AndroidUtils;

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
		if (!isInEditMode()) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
					android.R.layout.simple_dropdown_item_1line, AndroidUtils.getAccountsEmails());
			setAdapter(adapter);
			setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			setImeOptions(EditorInfo.IME_ACTION_NEXT);
		}
	}
	
	/**
	 * @see android.widget.AutoCompleteTextView#enoughToFilter()
	 */
	@Override
	public boolean enoughToFilter() {
		return true;
	}
	
	/**
	 * @see android.widget.AutoCompleteTextView#onFocusChanged(boolean, int, android.graphics.Rect)
	 */
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		
		if ((getWindowVisibility() != View.GONE) && focused) {
			performFiltering(getText(), 0);
			showDropDown();
		}
	}
	
}
