package com.jdroid.android.activity;

import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.search.SearchResult.PaginationListener;
import com.jdroid.android.search.SearchResult.SortingListener;
import com.jdroid.android.view.PaginationFooter;
import com.jdroid.java.exception.AbstractException;

/**
 * Base {@link ListActivity}
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public abstract class AbstractListActivity<T> extends ListActivity implements SortingListener<T>, OnItemClickListener,
		ActivityIf {
	
	private ActivityHelper activityHelper;
	private PaginationFooter paginationFooter;
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAndroidApplicationContext()
	 */
	@Override
	public DefaultApplicationContext getAndroidApplicationContext() {
		return activityHelper.getAndroidApplicationContext();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return activityHelper.onBeforeSetContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		activityHelper.onAfterSetContentView(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityHelper = AbstractApplication.get().createActivityHelper(this);
		activityHelper.beforeOnCreate();
		super.onCreate(savedInstanceState);
		activityHelper.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onContentChanged()
	 */
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		activityHelper.onContentChanged();
	}
	
	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		activityHelper.onSaveInstanceState(outState);
	}
	
	/**
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		activityHelper.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		activityHelper.onStart();
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@Override
	protected void onPause() {
		activityHelper.onBeforePause();
		super.onPause();
		activityHelper.onPause();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		activityHelper.onStop();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		activityHelper.onBeforeDestroy();
		super.onDestroy();
		activityHelper.onDestroy();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return activityHelper.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public Integer getMenuResourceId() {
		return activityHelper.getMenuResourceId();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		activityHelper.doOnCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return activityHelper.onOptionsItemSelected(item) ? true : super.onOptionsItemSelected(item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	public <V extends View> V findView(int id) {
		return activityHelper.findView(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showBlockingLoading()
	 */
	@Override
	public void showBlockingLoading() {
		activityHelper.showBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoading(LoadingDialogBuilder builder) {
		activityHelper.showLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissBlockingLoading()
	 */
	@Override
	public void dismissBlockingLoading() {
		activityHelper.dismissBlockingLoading();
	}
	
	@SuppressWarnings("unchecked")
	public T getMenuItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (T)getListView().getItemAtPosition(info.position);
	}
	
	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View,
	 *      int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		onListItemClick((ListView)parent, v, position, id);
	}
	
	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onListItemClick(ListView listView, View v, int position, long id) {
		int headersCount = listView.getHeaderViewsCount();
		int pos = position - headersCount;
		if ((pos >= 0) && (pos < listView.getAdapter().getCount())) {
			T t = (T)listView.getAdapter().getItem(pos);
			onListItemClick(t);
		}
	}
	
	protected void onListItemClick(T item) {
		// Do Nothing
	}
	
	@SuppressWarnings("unchecked")
	protected <M> M getSelectedItem(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		return (M)getListView().getItemAtPosition(info.position);
	}
	
	protected boolean hasPagination() {
		return false;
	}
	
	public SearchResult<T> getSearchResult() {
		return null;
	}
	
	/**
	 * @see android.app.ListActivity#setListAdapter(android.widget.ListAdapter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setListAdapter(ListAdapter adapter) {
		
		if (hasPagination()) {
			if (paginationFooter == null) {
				paginationFooter = (PaginationFooter)LayoutInflater.from(this).inflate(R.layout.pagination_footer, null);
				paginationFooter.setAbstractListActivity(this);
				getListView().addFooterView(paginationFooter, null, false);
			}
			getSearchResult().setPaginationListener((PaginationListener<T>)paginationFooter);
			paginationFooter.refresh();
		}
		super.setListAdapter(adapter);
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onStartSorting()
	 */
	@Override
	public void onStartSorting() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (hasPagination()) {
					paginationFooter.hide();
				}
				showBlockingLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onFinishSuccessfulSorting(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onFinishSuccessfulSorting(final List<T> items) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				BaseArrayAdapter<T> baseArrayAdapter = (BaseArrayAdapter<T>)getListAdapter();
				baseArrayAdapter.replaceAll(items);
				getListView().setSelectionAfterHeaderView();
				if (hasPagination()) {
					paginationFooter.refresh();
				}
				showBlockingLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.search.SearchResult.SortingListener#onFinishInvalidSorting(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishInvalidSorting(AbstractException androidException) {
		dismissBlockingLoading();
		throw androidException;
	}
	
	protected void addHeaderView(int resource) {
		getListView().addHeaderView(inflate(resource));
	}
	
	protected void addFooterView(int resource) {
		getListView().addFooterView(inflate(resource));
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return activityHelper.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return activityHelper.getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return activityHelper.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return activityHelper.requiresAuthentication();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return activityHelper.getUser();
	}
	
	public Boolean isAuthenticated() {
		return activityHelper.isAuthenticated();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return activityHelper.getAdSize();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return activityHelper.isLauncherActivity();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getLocationFrequency()
	 */
	@Override
	public Long getLocationFrequency() {
		return null;
	}
	
}