package com.jdroid.android.dialog;

import com.jdroid.android.dialog.ChoiceItemDialogFragment.ChoiceItem;

public interface ChoiceItemListener<T extends ChoiceItem> {
	
	/**
	 * This method is called when new item is selected
	 * 
	 * @param selectedItem
	 */
	public void onNewItemSelected(T selectedItem);
	
	/**
	 * This method is called when the selected item is the item that was previously selected
	 * 
	 * @param selectedItem
	 */
	public void onCurrentItemSelected(T selectedItem);
}
