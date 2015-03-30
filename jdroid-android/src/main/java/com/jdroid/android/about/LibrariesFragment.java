package com.jdroid.android.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class LibrariesFragment extends AbstractListFragment<Library> {
	
	private List<Library> libraries = Lists.newArrayList();
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		libraries.add(new Library("jdroid", R.string.jdroidTitle, R.string.jdroidDescription,
				"http://jdroidframework.com"));
		if (AbstractApplication.get().isImageLoaderEnabled()) {
			libraries.add(new Library("universalImageLoader", R.string.universalImageLoaderTitle,
					R.string.universalImageLoaderDescription, "https://github.com/nostra13/Android-Universal-Image-Loader"));
		}
		libraries.addAll(getCustomLibraries());
	}
	
	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new LibrariesAdapter(getActivity(), libraries));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(Library item) {
		item.onSelected(getActivity());
		AbstractApplication.get().getAnalyticsSender().trackAboutLibraryOpen(item.getLibraryKey());
		
	}
	
	protected Boolean displayImageLoader() {
		return AbstractApplication.get().isImageLoaderEnabled();
	}
	
	protected List<Library> getCustomLibraries() {
		return Lists.newArrayList();
	}
	
}
