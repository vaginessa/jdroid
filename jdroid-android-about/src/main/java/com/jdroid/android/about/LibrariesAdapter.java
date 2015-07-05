package com.jdroid.android.about;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.about.LibrariesAdapter.LibrariesHolder;
import com.jdroid.android.recycler.RecyclerViewAdapter;

import java.util.List;

public class LibrariesAdapter extends RecyclerViewAdapter<Library, LibrariesHolder> {
	
	public LibrariesAdapter(List<Library> items) {
		super(R.layout.library_item, items);
	}
	
	@Override
	protected void fillHolderFromItem(Library item, LibrariesHolder holder) {
		holder.name.setText(item.getNameResId());
		holder.description.setText(item.getDescriptionResId());
	}
	
	@Override
	protected LibrariesHolder createViewHolderFromView(View view) {
		LibrariesHolder holder = new LibrariesHolder(view);
		holder.name = findView(view, R.id.name);
		holder.description = findView(view, R.id.description);
		return holder;
	}

	public static class LibrariesHolder extends RecyclerView.ViewHolder {
		protected TextView name;
		protected TextView description;

		public LibrariesHolder(View itemView) {
			super(itemView);
		}
	}
}
