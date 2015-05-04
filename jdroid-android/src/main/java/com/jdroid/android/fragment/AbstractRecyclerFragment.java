package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.recycler.RecyclerViewAdapter;

/**
 * @param <T>
 */
public abstract class AbstractRecyclerFragment<T> extends AbstractFragment implements View.OnClickListener {

	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.recycler_vertical_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = findView(R.id.recyclerView);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerView.setHasFixedSize(true);

		// use a linear layout manager
		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

	}

	public void setAdapter(RecyclerViewAdapter adapter) {
		this.adapter = adapter;
		adapter.setOnClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		int itemPosition = recyclerView.getChildAdapterPosition(view);
		onItemSelected((T)adapter.getItem(itemPosition));
	}

	public void onItemSelected(T item) {
		// Do Nothing
	}
}

