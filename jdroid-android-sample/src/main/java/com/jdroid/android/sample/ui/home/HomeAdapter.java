package com.jdroid.android.sample.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;

public class HomeAdapter extends RecyclerViewAdapter<HomeItem, HomeAdapter.HomeHolder> {

	public HomeAdapter(HomeItem... items) {
		super(R.layout.home_item, Lists.newArrayList(items));
	}

	@Override
	protected void fillHolderFromItem(HomeItem item, HomeHolder holder) {
		holder.image.setImageResource(item.getIconResource());
		holder.name.setText(item.getNameResource());
	}

	@Override
	protected HomeHolder createViewHolderFromView(View view, int viewType) {
		HomeHolder holder = new HomeHolder(view);
		holder.image = findView(view, com.jdroid.android.R.id.image);
		holder.name = findView(view, com.jdroid.android.R.id.name);
		return holder;
	}

	public static class HomeHolder extends RecyclerView.ViewHolder {

		protected ImageView image;
		protected TextView name;

		public HomeHolder(View itemView) {
			super(itemView);
		}
	}
}
