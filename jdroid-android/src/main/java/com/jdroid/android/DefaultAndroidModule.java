package com.jdroid.android;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.jdroid.android.cookie.CookieRepository;
import com.jdroid.android.cookie.CookieRepositoryImpl;

public class DefaultAndroidModule extends AbstractModule {
	
	/**
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		if (AbstractApplication.get().getAndroidApplicationContext().isCookieRepositoryEnabled()) {
			this.bind(CookieRepository.class).to(CookieRepositoryImpl.class).in(Singleton.class);
		}
	}
	
}
