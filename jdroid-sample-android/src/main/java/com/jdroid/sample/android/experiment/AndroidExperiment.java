package com.jdroid.sample.android.experiment;

import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.analytics.SimpleExperimentVariant;
import com.jdroid.java.collections.Lists;

import java.util.List;

public enum AndroidExperiment implements ExperimentHelper.Experiment {

	SAMPLE_EXPERIMENT("sampleExperiment", Lists.<ExperimentHelper.ExperimentVariant>newArrayList(SimpleExperimentVariant.A, SimpleExperimentVariant.B), 9);

	private String id;
	private List<ExperimentHelper.ExperimentVariant> variants;
	private Integer customDimensionIndex;

	private AndroidExperiment(String id, List<ExperimentHelper.ExperimentVariant> variants, Integer customDimensionIndex) {
		this.id = id;
		this.variants = variants;
		this.customDimensionIndex = customDimensionIndex;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<ExperimentHelper.ExperimentVariant> getVariants() {
		return variants;
	}

	@Override
	public Integer getCustomDimensionIndex() {
		return customDimensionIndex;
	}
}
