package com.jdroid.android.analytics;

import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExperimentHelper {
	
	public interface Experiment {
		
		public String getId();
		
		public List<ExperimentVariant> getVariants();
	}
	
	public interface ExperimentVariant {
		
		public String getId();
	}
	
	private static Map<Experiment, ExperimentVariant> experimentsMap = Maps.newHashMap();
	
	public static void registerExperiment(Experiment experiment) {
		
		String experimentVariantValue = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			experiment.getId(), null);
		ExperimentVariant experimentVariant = null;
		if (experimentVariantValue == null) {
			experimentVariant = experiment.getVariants().get(new Random().nextInt(experiment.getVariants().size()));
			Editor editor = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).edit();
			editor.putString(experiment.getId(), experimentVariant.getId());
			editor.apply();
		} else {
			for (ExperimentVariant each : experiment.getVariants()) {
				if (each.getId().equals(experimentVariantValue)) {
					experimentVariant = each;
					break;
				}
			}
		}
		
		if (experimentVariant != null) {
			experimentsMap.put(experiment, experimentVariant);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ExperimentVariant> T getExperimentVariant(Experiment experiment) {
		if (experimentsMap != null) {
			return (T)experimentsMap.get(experiment);
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("experimentsMap is null");
		}
		return null;
	}
	
	public static Map<Experiment, ExperimentVariant> getExperimentsMap() {
		return experimentsMap;
	}
}
