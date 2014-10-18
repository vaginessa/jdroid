package com.jdroid.java.scheduler;

import java.util.Date;

public class AbstractScheduler {
	
	private Boolean inProgress = false;
	private Date executionStartDate;
	private Date executionEndDate;
	
	public synchronized Boolean acquireLock() {
		if (!inProgress) {
			inProgress = true;
			executionStartDate = new Date();
			return true;
		}
		return false;
	}
	
	public synchronized void releaseLock() {
		inProgress = false;
		executionEndDate = new Date();
	}
	
	public Date getLastExecutionStartDate() {
		return executionStartDate;
	}
	
	public Date getLastExdcutionEndDate() {
		return executionEndDate;
	}
	
}
