package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.usecase.PaginatedUseCase;
import com.jdroid.java.exception.AbstractException;

import java.util.List;

/**
 * 
 * @param <T>
 */
public abstract class AbstractPaginatedGridFragment<T> extends AbstractGridFragment<T> {
	
	private View paginationFooter;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// FIXME When rotating the loading is missed
		paginationFooter = findView(R.id.progressBar);
		paginationFooter.setVisibility(View.GONE);
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
		
		GridView gridView = getGridView();
		if (gridView != null) {
			gridView.setOnScrollListener(new OnScrollListener() {
				
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
					
					if (((firstVisibleItem + visibleItemCount) == totalItemCount)
							&& getPaginatedUseCase().isInProgress()) {
						paginationFooter.setVisibility(View.VISIBLE);
					} else {
						paginationFooter.setVisibility(View.GONE);
					}
				}
			});
		}
	}
	
	protected abstract PaginatedUseCase<T> getPaginatedUseCase();
	
	protected abstract BaseArrayAdapter<T> createBaseArrayAdapter(List<T> items);
	
	@WorkerThread
	@Override
	public void onStartUseCase() {
		if (getPaginatedUseCase().isPaginating()) {
			startPaginationLoading();
		} else {
			super.onStartUseCase();
		}
	}
	
	@WorkerThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (getPaginatedUseCase().isPaginating()) {
			AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
			dismissPaginationLoading();
		} else {
			super.onFinishFailedUseCase(abstractException);
		}
	}
	
	@WorkerThread
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				if (getPaginatedUseCase().isPaginating()) {
					((BaseArrayAdapter<T>)getListAdapter()).addAll(getPaginatedUseCase().getResults());
					dismissLoading();
				} else {
					
					if ((getItemsToAutoPaginate() != null)
							&& (getPaginatedUseCase().getResults().size() <= getItemsToAutoPaginate())) {
						
						if (getPaginatedUseCase().getResults().isEmpty()) {
							setListAdapter(createBaseArrayAdapter(getPaginatedUseCase().getResults()));
						} else {
							setListAdapter(createBaseArrayAdapter(getPaginatedUseCase().getResults()));
							dismissLoading();
						}
						
						getPaginatedUseCase().markAsPaginating();
						executeUseCase(getPaginatedUseCase());
					} else {
						setListAdapter(createBaseArrayAdapter(getPaginatedUseCase().getResults()));
						dismissLoading();
					}
				}
				dismissPaginationLoading();
			}
		});
	}
	
	protected Integer getItemsToAutoPaginate() {
		return null;
	}
	
	protected void startPaginationLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				paginationFooter.setVisibility(View.VISIBLE);
			}
		});
	}
	
	protected void dismissPaginationLoading() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				paginationFooter.setVisibility(View.GONE);
			}
		});
	}
	
}