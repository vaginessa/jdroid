package com.jdroid.android.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.usecase.PaginatedUseCase;
import com.jdroid.java.exception.AbstractException;

import java.util.List;

public abstract class AbstractPaginatedRecyclerFragment extends AbstractRecyclerFragment {
	
	protected PaginatedUseCase<Object> paginatedUseCase;
	private int loadingItemViewType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		paginatedUseCase = createPaginatedUseCase();
	}

	@Override
	public void onStart() {
		super.onStart();
		registerUseCase(paginatedUseCase, this, getUseCaseTrigger());
	}

	protected FragmentHelper.UseCaseTrigger getUseCaseTrigger() {
		return FragmentHelper.UseCaseTrigger.ONCE;
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterUseCase(paginatedUseCase, this);
	}

	protected abstract PaginatedUseCase<Object> createPaginatedUseCase();

	protected abstract RecyclerViewAdapter createAdapter(List<Object> items);

	private void setAdapter() {
		RecyclerViewAdapter adapter = createAdapter(paginatedUseCase.getResults());
		loadingItemViewType = adapter.addRecyclerViewType(new LoadingRecyclerViewType());
		setAdapter(adapter);
	}

	@Override
	public void onStartUseCase() {
		if (paginatedUseCase.isPaginating()) {
			executeOnUIThread(new Runnable() {
				@Override
				public void run() {
					// Add loading footer
					int lastPosition = getAdapter().getItemCount() - 1;
					if (lastPosition >= 0) {
						int itemViewType = getAdapter().getItemViewType(getAdapter().getItemCount() - 1);
						if (itemViewType != loadingItemViewType) {
							getAdapter().addItem(new Object());
						}
					}
				}
			});
		} else {
			super.onStartUseCase();
		}
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (paginatedUseCase.isPaginating()) {
			AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
			executeOnUIThread(new Runnable() {
				@Override
				public void run() {
					dismissPaginationLoading();
				}
			});
		} else {
			super.onFinishFailedUseCase(abstractException);
		}
	}

	private void dismissPaginationLoading() {
		int lastPosition = getAdapter().getItemCount() - 1;
		if (lastPosition >= 0) {
			int itemViewType = getAdapter().getItemViewType(getAdapter().getItemCount() - 1);
			if (itemViewType == loadingItemViewType) {
				getAdapter().removeItemByPosition(lastPosition);
			}
		}
	}
	
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				if (paginatedUseCase.isPaginating()) {

					dismissPaginationLoading();

					getAdapter().addItems(paginatedUseCase.getResults());
					dismissLoading();
				} else {
					
					if ((getItemsToAutoPaginate() != null) && (paginatedUseCase.getResults().size() <= getItemsToAutoPaginate())) {
						if (paginatedUseCase.getResults().isEmpty()) {
							setAdapter();
						} else {
							setAdapter();
							dismissLoading();
						}
						paginatedUseCase.markAsPaginating();
						executeUseCase(paginatedUseCase);
					} else {
						setAdapter();
						dismissLoading();
					}
				}
			}
		});
	}

	@Override
	public void setAdapter(RecyclerViewAdapter adapter) {
		super.setAdapter(adapter);

		RecyclerView recyclerView = getRecyclerView();
		if (recyclerView != null) {
			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					if (!paginatedUseCase.isInProgress() && !paginatedUseCase.isLastPage()) {

						int visibleItemCount = getLayoutManager().getChildCount();
						int totalItemCount = getLayoutManager().getItemCount();
						int firstVisibleItemPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();

						if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4 && firstVisibleItemPosition >= 0) {
							paginatedUseCase.markAsPaginating();
							executeUseCase(paginatedUseCase);
						}
					}
				}
			});
		}
	}

	protected Integer getItemsToAutoPaginate() {
		return null;
	}

	protected int getPaginationFooterResId() {
		return R.layout.pagination_footer;
	}
	
	public class LoadingRecyclerViewType extends SimpleRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return AbstractPaginatedRecyclerFragment.this.getPaginationFooterResId();
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return AbstractPaginatedRecyclerFragment.this;
		}

		@Override
		protected Boolean isClickable() {
			return false;
		}
	}
}
