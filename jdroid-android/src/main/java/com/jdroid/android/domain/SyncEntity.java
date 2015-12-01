package com.jdroid.android.domain;

public class SyncEntity extends Entity {

	private SyncStatus syncStatus;

	public void markAsSynced() {
		syncStatus = SyncStatus.SYNCED;
	}

	public void setSyncStatus(SyncStatus syncStatus) {
		this.syncStatus = syncStatus;
	}

	public SyncStatus getSyncStatus() {
		return syncStatus;
	}
}
