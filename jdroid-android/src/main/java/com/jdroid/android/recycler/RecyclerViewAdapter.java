package com.jdroid.android.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Map<Integer, RecyclerViewType> recyclerViewTypeMap = Maps.newHashMap();

	private List<Object> items;

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType) {
		this(Lists.newArrayList(recyclerViewType), Lists.newArrayList());
	}

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType, List<? extends Object> items) {
		this(Lists.newArrayList(recyclerViewType), items);
	}

	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes) {
		this(recyclerViewTypes, Lists.newArrayList());
	}
	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes, List<? extends Object> items) {
		this.items = (List<Object>)items;
		int i = 1;
		for (RecyclerViewType each : recyclerViewTypes) {
			recyclerViewTypeMap.put(i, each);
			i++;
		}
	}

	public int addRecyclerViewType(RecyclerViewType recyclerViewType) {
		int viewType = recyclerViewTypeMap.size() + 1;
		recyclerViewTypeMap.put(viewType, recyclerViewType);
		return viewType;
	}

	@Override
	public int getItemViewType(int position) {
		Object item = items.get(position);
		Class eachClass = item.getClass();
		while (eachClass != null) {
			for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
				if (entry.getValue().getItemClass().equals(eachClass)) {
					return entry.getKey();
				}
			}
			eachClass = eachClass.getSuperclass();
		}
		return -1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = recyclerViewTypeMap.get(viewType).inflateView(inflater, parent);
		return recyclerViewTypeMap.get(viewType).createViewHolderFromView(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		recyclerViewTypeMap.get(holder.getItemViewType()).fillHolderFromItem(items.get(position), holder);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public <T> void  addItem(T item) {
		items.add(item);
		notifyItemInserted(items.size() - 1);
	}

	public <T> void addItems(List<T> newItems) {
		items.addAll(newItems);
		notifyItemRangeInserted(items.size() - newItems.size(), newItems.size());
	}

	public <T> void removeItem(T item) {
		int pos = items.indexOf(item);
		items.remove(item);
		notifyItemRemoved(pos);
	}

	public void removeItemByPosition(int position) {
		items.remove(position);
		notifyItemRemoved(position);
	}

	public void clear() {
		int size = items.size();
		items.clear();
		notifyItemRangeRemoved(0, size);
	}

	public List<Object> getItems() {
		return items;
	}

	public Object getItem(Integer position) {
		return items.get(position);
	}
}
