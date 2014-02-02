package com.jdroid.android.usecase;

import java.util.List;
import org.slf4j.Logger;
import com.jdroid.android.search.SortingType;
import com.jdroid.java.search.PagedResult;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class PaginatedUseCase<T> extends DefaultAbstractUseCase {
	
	public enum PaginatedUseCaseMode {
		INITIAL_LOAD,
		PAGINATION,
		SORTING;
	}
	
	private final static Logger LOGGER = LoggerUtils.getLogger(PaginatedUseCase.class);
	
	private final static int DEFAULT_PAGE_SIZE = 50;
	
	private PaginatedUseCaseMode paginatedUseCaseMode = PaginatedUseCaseMode.INITIAL_LOAD;
	private int page = 1;
	private SortingType sortingType;
	
	private PagedResult<T> pagedResult;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected final void doExecute() {
		
		if (paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION)) {
			page++;
		} else {
			page = 1;
		}
		
		try {
			pagedResult = doPopulate(page, getPageSize(), sortingType);
			LOGGER.debug("Results: " + pagedResult.getResults().size() + " / Page: " + page + " / Sorting: "
					+ sortingType);
		} catch (RuntimeException e) {
			if (paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION)) {
				page--;
			}
			throw e;
		}
	}
	
	protected abstract PagedResult<T> doPopulate(int page, int pageSize, SortingType sortingType);
	
	public void markAsPaginating() {
		paginatedUseCaseMode = PaginatedUseCaseMode.PAGINATION;
	}
	
	public void reset() {
		paginatedUseCaseMode = PaginatedUseCaseMode.INITIAL_LOAD;
	}
	
	public Boolean isPaginating() {
		return paginatedUseCaseMode.equals(PaginatedUseCaseMode.PAGINATION);
	}
	
	/**
	 * @return Whether this list is on the last page or not
	 */
	public boolean isLastPage() {
		return pagedResult.isLastPage();
	}
	
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}
	
	/**
	 * @return the results
	 */
	public List<T> getResults() {
		return pagedResult.getResults();
	}
	
	/**
	 * @param sortingType the sortingType to set
	 */
	public void setSortingType(SortingType sortingType) {
		this.sortingType = sortingType;
	}
	
}
