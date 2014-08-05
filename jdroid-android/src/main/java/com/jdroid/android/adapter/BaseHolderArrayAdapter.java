package com.jdroid.android.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * {@link BaseArrayAdapter} for lists that takes care of reusing the items' views.
 * 
 * @param <ITEM> An item in the list.
 * @param <VIEWHOLDER> A view holder containing all the necessary {@link View}s for an item's layout.
 * 
 */
public abstract class BaseHolderArrayAdapter<ITEM, VIEWHOLDER> extends BaseArrayAdapter<ITEM> {
	
	/** The {@link LayoutInflater} used to inflate the items in the list */
	private LayoutInflater layoutInflater;
	
	/**
	 * The resource indicating what views to inflate to display the content of this array adapter.
	 */
	private int resource;
	
	/**
	 * The resource indicating what views to inflate to display the content of this array adapter in a drop down widget.
	 */
	private int dropDownResource;
	
	public BaseHolderArrayAdapter(Context context, int resource) {
		this(context, resource, null);
	}
	
	public BaseHolderArrayAdapter(Context context, int resource, List<ITEM> items) {
		super(context, items);
		layoutInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.dropDownResource = resource;
	}
	
	/**
	 * @see ArrayAdapter#getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, resource);
	}
	
	/**
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, dropDownResource);
	}
	
	@SuppressWarnings("unchecked")
	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		VIEWHOLDER holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(resource, parent, false);
			
			holder = createViewHolderFromConvertView(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (VIEWHOLDER)convertView.getTag();
		}
		
		ITEM item = getItem(position);
		fillHolderFromItem(item, holder);
		if (getCount() == 1) {
			onUniqueListItem(convertView, holder);
		} else {
			if (position == 0) {
				onFirstListItem(convertView, holder);
			} else if ((position + 1) == getCount()) {
				onLastListItem(convertView, holder);
			} else {
				onMiddleListItem(convertView, holder);
			}
		}
		
		if ((position % 2) == 0) {
			onEvenListItem(convertView, holder);
		} else {
			onOddListItem(convertView, holder);
		}
		onListItem(convertView, holder);
		return convertView;
	}
	
	/**
	 * @see android.widget.ArrayAdapter#setDropDownViewResource(int)
	 */
	@Override
	public void setDropDownViewResource(int resource) {
		this.dropDownResource = resource;
	}
	
	/**
	 * Called when the view of the unique item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onUniqueListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the first item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onFirstListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the middle item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onMiddleListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of the last item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onLastListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an odd item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onOddListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an even item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onEvenListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Called when the view of an item of the list is created
	 * 
	 * @param view The View corresponding to the data at the specified position.
	 * @param holder The VIEWHOLDER.
	 */
	protected void onListItem(View view, VIEWHOLDER holder) {
		// Do Nothing
	}
	
	/**
	 * Fills the VIEWHOLDER with the ITEM's data.
	 * 
	 * @param item The ITEM.
	 * @param holder The VIEWHOLDER.
	 */
	protected abstract void fillHolderFromItem(ITEM item, VIEWHOLDER holder);
	
	/**
	 * Creates a VIEWHOLDER from the given convertView. Please declare the VIEWHOLDER class as static when possible
	 * 
	 * @param convertView The convertView from the list.
	 * @return The new VIEWHOLDER.
	 */
	protected abstract VIEWHOLDER createViewHolderFromConvertView(View convertView);
	
}