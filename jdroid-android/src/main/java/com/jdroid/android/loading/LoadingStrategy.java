package com.jdroid.android.loading;

import com.jdroid.android.fragment.FragmentIf;

public enum LoadingStrategy {
	BLOCKING {
		
		@Override
		public void showLoading(FragmentIf fragmentIf) {
			fragmentIf.showBlockingLoading();
		}
		
		@Override
		public void dismissLoading(FragmentIf fragmentIf) {
			fragmentIf.dismissBlockingLoading();
		}
	},
	NON_BLOCKING {
		
		@Override
		public void showLoading(FragmentIf fragmentIf) {
			fragmentIf.showNonBlockingLoading();
		}
		
		@Override
		public void dismissLoading(FragmentIf fragmentIf) {
			fragmentIf.dismissNonBlockingLoading();
		}
	},
	SWIPE_REFRESH {
		
		@Override
		public void showLoading(FragmentIf fragmentIf) {
			fragmentIf.showSwipeRefreshLoading();
		}
		
		@Override
		public void dismissLoading(FragmentIf fragmentIf) {
			fragmentIf.dismisSwipeRefreshLoading();
		}
	};
	
	public abstract void showLoading(FragmentIf fragmentIf);
	
	public abstract void dismissLoading(FragmentIf fragmentIf);
}
