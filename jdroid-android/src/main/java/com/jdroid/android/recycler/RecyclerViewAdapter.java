package com.jdroid.android.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerViewAdapter<ITEM, VIEWHOLDER extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VIEWHOLDER> {

	private List<ITEM> items;

	// The resource indicating what views to inflate to display the content of this adapter.
	private Integer resource;

	private View.OnClickListener onClickListener;

	public RecyclerViewAdapter(Integer resource, List<ITEM> items) {
		this.resource = resource;
		this.items = items;
	}

	@Override
	public VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
		if (onClickListener != null) {
			view.setOnClickListener(onClickListener);
		}
		return createViewHolderFromView(view);
	}

	/**
	 * Creates a VIEWHOLDER from the given view. Please declare the VIEWHOLDER class as static when possible
	 *
	 * @param view The view from the list.
	 * @return The new VIEWHOLDER.
	 */
	protected abstract VIEWHOLDER createViewHolderFromView(View view);

	@Override
	public void onBindViewHolder(VIEWHOLDER holder, int position) {
		fillHolderFromItem(items.get(position), holder);
	}

	/**
	 * Fills the VIEWHOLDER with the ITEM's data.
	 *
	 * @param item The ITEM.
	 * @param holder The VIEWHOLDER.
	 */
	protected abstract void fillHolderFromItem(ITEM item, VIEWHOLDER holder);

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void addItem(ITEM item) {
		items.add(item);
		notifyItemInserted(items.size() - 1);
	}

	public void removeItem(ITEM item) {
		int pos = items.indexOf(item);
		items.remove(item);
		notifyItemRemoved(pos);
	}

	public List<ITEM> getItems() {
		return items;
	}

	public ITEM getItem(Integer position) {
		return items.get(position);
	}

	/**
	 * Finds a view that was identified by the id attribute from the {@link View} view.
	 *
	 * @param containerView The view that contains the view to find.
	 * @param id The id to search for.
	 * @param <V> The {@link View} class.
	 *
	 * @return The view if found or null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(View containerView, int id) {
		return (V)containerView.findViewById(id);
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}
