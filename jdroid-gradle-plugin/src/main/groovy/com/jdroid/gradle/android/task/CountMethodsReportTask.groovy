package com.jdroid.gradle.android.task

import com.jdroid.android.dex.DexMethodCounts

public class CountMethodsReportTask extends AbstractCountMethodsTask {

	public CountMethodsReportTask() {
		description = 'Output per-package method counts in an Android DEX executable grouped by package.'
	}

	@Override
	protected void doGenerateReport(String apkFilePath) {
		File outputDir = project.file(project.reportsDir);
		outputDir.mkdirs();
		File outputFile = project.file(outputDir.getAbsolutePath() + "/" + project.file(apkFilePath).getName() - '.apk' + '-methodCount.txt');

		DexMethodCounts.generateFullReport(apkFilePath, outputFile);
	}
}
