package com.jdroid.android.about;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.R;
import com.jdroid.android.about.LibrariesAdapter.LibrariesHolder;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;

public class LibrariesAdapter extends BaseHolderArrayAdapter<Library, LibrariesHolder> {
	
	public LibrariesAdapter(Context context, List<Library> items) {
		super(context, R.layout.library_item, items);
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#fillHolderFromItem(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void fillHolderFromItem(Library item, LibrariesHolder holder) {
		holder.name.setText(item.getNameResId());
		holder.description.setText(item.getDescriptionResId());
	}
	
	/**
	 * @see com.jdroid.android.adapter.BaseHolderArrayAdapter#createViewHolderFromConvertView(android.view.View)
	 */
	@Override
	protected LibrariesHolder createViewHolderFromConvertView(View convertView) {
		LibrariesHolder holder = new LibrariesHolder();
		holder.name = findView(convertView, R.id.name);
		holder.description = findView(convertView, R.id.description);
		return holder;
	}
	
	public static class LibrariesHolder {
		
		protected TextView name;
		protected TextView description;
	}
}
