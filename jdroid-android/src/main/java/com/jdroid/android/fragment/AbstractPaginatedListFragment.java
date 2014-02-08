package com.jdroid.android.fragment;

import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.usecase.PaginatedUseCase;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class AbstractPaginatedListFragment<T> extends AbstractListFragment<T> {
	
	private View paginationFooter;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// FIXME When rotating the loading is missed
		paginationFooter = inflate(R.layout.pagination_footer);
		paginationFooter.setVisibility(View.GONE);
		getListView().addFooterView(paginationFooter, null, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(getPaginatedUseCase(), this, getUseCaseTrigger());
	}
	
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.ONCE;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(getPaginatedUseCase(), this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#setListAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setListAdapter(ListAdapter adapter) {
		super.setListAdapter(adapter);
		
		ListView listView = getListView();
		if (listView != null) {
			listView.setOnScrollListener(new OnScrollListener() {
				
				private int scrollState;
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					this.scrollState = scrollState;
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					int lastItemIndex = firstVisibleItem + visibleItemCount;
					if ((scrollState != SCROLL_STATE_IDLE) && ((totalItemCount - lastItemIndex) < 4)
							&& !getPaginatedUseCase().isInProgress() && !getPaginatedUseCase().isLastPage()) {
						getPaginatedUseCase().markAsPaginating();
						executeUseCase(getPaginatedUseCase());
					}
				}
			});
		}
	}
	
	protected abstract PaginatedUseCase<T> getPaginatedUseCase();
	
	protected abstract BaseArrayAdapter<T> createBaseArrayAdapter(List<T> items);
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		if (getPaginatedUseCase().isPaginating()) {
			startPaginationLoading();
		} else {
			super.onStartUseCase();
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		if (getPaginatedUseCase().isPaginating()) {
			AbstractApplication.get().getExceptionHandler().logHandledException(runtimeException);
			dismissPaginationLoading();
		} else {
			super.onFinishFailedUseCase(runtimeException);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				if (getPaginatedUseCase().isPaginating()) {
					((BaseArrayAdapter<T>)getListAdapter()).addAll(getPaginatedUseCase().getResults());
				} else {
					setListAdapter(createBaseArrayAdapter(getPaginatedUseCase().getResults()));
					dismissLoading();
				}
				dismissPaginationLoading();
			}
		});
	}
	
	private void startPaginationLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				paginationFooter.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void dismissPaginationLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				paginationFooter.setVisibility(View.GONE);
			}
		});
	}
	
}