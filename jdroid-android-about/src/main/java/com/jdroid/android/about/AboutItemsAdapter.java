package com.jdroid.android.about;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.about.AboutItemsAdapter.AboutItemHolder;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;

import java.util.List;

public class AboutItemsAdapter extends BaseHolderArrayAdapter<AboutItem, AboutItemHolder> {
	
	public AboutItemsAdapter(Context context, List<AboutItem> items) {
		super(context, R.layout.default_item, items);
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(AboutItem item, AboutItemHolder holder) {
		holder.image.setImageResource(item.getIconResId());
		holder.name.setText(item.getNameResId());
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected AboutItemHolder createViewHolderFromConvertView(View convertView) {
		AboutItemHolder holder = new AboutItemHolder();
		holder.image = findView(convertView, R.id.image);
		holder.name = findView(convertView, R.id.name);
		return holder;
	}
	
	public static class AboutItemHolder {
		
		protected ImageView image;
		protected TextView name;
	}
}
