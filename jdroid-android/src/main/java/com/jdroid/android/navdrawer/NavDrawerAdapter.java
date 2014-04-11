package com.jdroid.android.navdrawer;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.navdrawer.NavDrawerAdapter.NavDrawerAdapterHolder;

/**
 * 
 * @author Maxi Rosson
 */
public class NavDrawerAdapter extends BaseHolderArrayAdapter<NavDrawerItem, NavDrawerAdapterHolder> {
	
	public NavDrawerAdapter(Context context, List<NavDrawerItem> items) {
		super(context, R.layout.nav_drawer_item, items);
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(NavDrawerItem item, NavDrawerAdapterHolder holder) {
		if (item.getIconResource() != null) {
			holder.image.setImageResource(item.getIconResource());
		}
		if (item.getNameResource() != null) {
			holder.name.setText(item.getNameResource());
		}
		if (item.isMainAction()) {
			holder.name.setTextSize(18);
			holder.convertView.setBackgroundResource(R.drawable.nav_drawer_main_item_selector);
		} else {
			holder.name.setTextSize(16);
			holder.convertView.setBackgroundResource(R.drawable.nav_drawer_secondary_item_selector);
		}
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected NavDrawerAdapterHolder createViewHolderFromConvertView(View convertView) {
		NavDrawerAdapterHolder holder = new NavDrawerAdapterHolder();
		holder.convertView = convertView;
		holder.image = findView(convertView, R.id.image);
		holder.name = findView(convertView, R.id.name);
		return holder;
	}
	
	public static class NavDrawerAdapterHolder {
		
		protected View convertView;
		protected ImageView image;
		protected TextView name;
	}
	
}
