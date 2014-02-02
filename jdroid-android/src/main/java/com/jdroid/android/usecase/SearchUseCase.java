package com.jdroid.android.usecase;

import java.util.Set;
import com.jdroid.android.search.SortingType;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.search.PagedResult;

/**
 * 
 * @author Maxi Rosson
 * @param <T> item to search for
 */
public abstract class SearchUseCase<T> extends PaginatedUseCase<T> {
	
	private String searchValue;
	private Set<T> selectedItems = Sets.newHashSet();
	
	/**
	 * @see com.jdroid.android.usecase.PaginatedUseCase#doPopulate(int, int, com.jdroid.android.search.SortingType)
	 */
	@Override
	protected final PagedResult<T> doPopulate(int page, int pageSize, SortingType sortingType) {
		return doSearch(searchValue, page, pageSize, sortingType);
	}
	
	protected abstract PagedResult<T> doSearch(String searchValue, int page, int pageSize, SortingType sortingType);
	
	/**
	 * @param searchValue the searchValue to set
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public Set<T> getSelectedItems() {
		return selectedItems;
	}
	
	public void setSelectedItems(Set<T> selectedItems) {
		this.selectedItems = selectedItems;
	}
	
	/**
	 * @return the searchValue
	 */
	public String getSearchValue() {
		return searchValue;
	}
}
