package com.jdroid.android.provider;

import android.content.Context;
import com.jdroid.android.R;

public class RefreshActionProvider extends TwoStateActionProvider {
	
	public RefreshActionProvider(Context context) {
		super(context);
	}
	
	/**
	 * @see com.jdroid.android.provider.TwoStateActionProvider#getFirstStateImageResId()
	 */
	@Override
	protected Integer getFirstStateImageResId() {
		return R.drawable.ic_menu_refresh;
	}
	
	/**
	 * @see com.jdroid.android.provider.TwoStateActionProvider#getFirstStateCheatSheetResId()
	 */
	@Override
	protected Integer getFirstStateCheatSheetResId() {
		return R.string.refresh;
	}
}
