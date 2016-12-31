package com.jdroid.java.analytics;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class AnalyticsSender<T extends AnalyticsTracker> implements AnalyticsTracker {

	private static final Logger LOGGER = LoggerUtils.getLogger(AnalyticsSender.class);

	private List<T> trackers = Lists.newArrayList();

	@SafeVarargs
	public AnalyticsSender(T... trackers) {
		this(Lists.newArrayList(trackers));
	}

	public AnalyticsSender(List<T> trackers) {
		for (T tracker : trackers) {
			if (tracker.isEnabled()) {
				this.trackers.add(tracker);
			}
		}
	}

	@Override
	public Boolean isEnabled() {
		return null;
	}

	protected List<T> getTrackers() {
		return trackers;
	}

	public void addTracker(T tracker) {
		trackers.add(tracker);
	}

	public abstract class TrackerRunnable implements Runnable {

		@Override
		public void run() {
			for (T tracker : getTrackers()) {
				try {
					if (tracker.isEnabled()) {
						track(tracker);
					}
				} catch (Exception e) {
					LoggerUtils.logHandledException(LOGGER, e);
				}
			}
		}

		protected abstract void track(T tracker);
	}

}
