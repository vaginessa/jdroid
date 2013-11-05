package com.jdroid.android.repository;

import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.InMemoryRepository;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class SynchronizedInMemoryRepository<T extends Identifiable> extends InMemoryRepository<T> implements
		SynchronizedRepository<T> {
	
	// Default Refresh frequency (in milliseconds)
	private static final Long DEFAULT_REFRESH_FREQUENCY = 5 * DateUtils.MILLIS_PER_MINUTE; // 5 min
	
	private Long lastUpdateTimestamp;
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#refreshUpdateTimestamp()
	 */
	@Override
	public void refreshUpdateTimestamp() {
		lastUpdateTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#isOutdated()
	 */
	@Override
	public Boolean isOutdated() {
		if ((lastUpdateTimestamp == null)
				|| ((lastUpdateTimestamp + getRefreshFrequency()) < System.currentTimeMillis())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#getLastUpdateTimestamp()
	 */
	@Override
	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#resetLastUpdateTimestamp()
	 */
	@Override
	public void resetLastUpdateTimestamp() {
		lastUpdateTimestamp = null;
	}
	
	protected Long getRefreshFrequency() {
		return DEFAULT_REFRESH_FREQUENCY;
	}
}
