package com.jdroid.android.view;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.search.SearchResult.PaginationListener;
import com.jdroid.java.exception.AbstractException;

/**
 * 
 * @author Maxi Rosson
 */
public class PaginationFooter extends LinearLayout implements PaginationListener<Object> {
	
	private AbstractListFragment<?> abstractListFragment;
	private Boolean loading = false;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public PaginationFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * @see android.widget.TextView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!loading && !abstractListFragment.getSearchResult().isLastPage()) {
			loading = true;
			abstractListFragment.getSearchResult().nextPage();
		}
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.PaginationListener#onStartPagination()
	 */
	@Override
	public void onStartPagination() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.PaginationListener#onFinishSuccessfulPagination(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onFinishSuccessfulPagination(final List<Object> items) {
		refresh();
		abstractListFragment.executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				BaseArrayAdapter<Object> baseArrayAdapter = (BaseArrayAdapter<Object>)abstractListFragment.getListAdapter();
				baseArrayAdapter.add(items);
			}
		});
		loading = false;
	}
	
	public void refresh() {
		abstractListFragment.executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				if (abstractListFragment.getSearchResult().isLastPage()) {
					hide();
				} else {
					show();
				}
			}
		});
	}
	
	public void hide() {
		// REVIEW: see if we can hide the whole footer
		findViewById(R.id.progressBar).setVisibility(View.GONE);
	}
	
	private void show() {
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.PaginationListener#onFinishInvalidPagination(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishInvalidPagination(AbstractException androidException) {
		refresh();
		loading = false;
		throw androidException;
	}
	
	/**
	 * @param abstractListFragment the abstractListFragment to set
	 */
	public void setAbstractListFragment(AbstractListFragment<?> abstractListFragment) {
		this.abstractListFragment = abstractListFragment;
	}
	
}
