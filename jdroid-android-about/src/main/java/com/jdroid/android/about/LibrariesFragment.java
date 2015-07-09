package com.jdroid.android.about;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.android.images.loader.ImageLoaderHelper;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class LibrariesFragment extends AbstractRecyclerFragment<Library> {
	
	private List<Library> libraries = Lists.newArrayList();
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		libraries.add(new Library("jdroid", R.string.jdroidTitle, R.string.jdroidDescription,
				"http://jdroidframework.com"));
		if (displayImageLoader()) {
			ImageLoaderHelper imageLoaderHelper = AbstractApplication.get().getImageLoaderHelper();
			Library library = new Library(imageLoaderHelper.getLibraryKey(), imageLoaderHelper.getLibraryNameResId(),
					imageLoaderHelper.getLibraryDescriptionResId(), imageLoaderHelper.getLibraryUrl());
			if (library != null) {
				libraries.add(library);
			}
		}
		libraries.addAll(getCustomLibraries());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(new LibrariesAdapter(libraries));
	}
	
	@Override
	public void onItemSelected(Library item, View view) {
		item.onSelected(getActivity());
		AbstractApplication.get().getAnalyticsSender().trackAboutLibraryOpen(item.getLibraryKey());
		
	}
	
	protected Boolean displayImageLoader() {
		return AbstractApplication.get().getImageLoaderHelper() != null;
	}
	
	protected List<Library> getCustomLibraries() {
		return Lists.newArrayList();
	}
	
}
