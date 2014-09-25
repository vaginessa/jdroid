package com.jdroid.android.loading;

public enum FragmentLoadingFactory {
	
	NON_BLOCKING {
		
		@Override
		public FragmentLoading create() {
			return new NonBlockingLoading();
		}
	},
	SWIPE_REFRESH {
		
		@Override
		public FragmentLoading create() {
			return new SwipeRefreshLoading();
		}
	};
	
	public abstract FragmentLoading create();
}
