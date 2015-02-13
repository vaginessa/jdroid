package com.jdroid.gradle.android.task

import info.persistent.dex.DexMethodCounts

public class CountMethodsSummaryTask extends AbstractCountMethodsTask {

	public CountMethodsReportTask() {
		description = 'Output the method counts in an Android DEX executable'
	}

	@Override
	protected void doGenerateReport(String apkFilePath) {
		DexMethodCounts.generateSummary(apkFilePath);
	}
}
