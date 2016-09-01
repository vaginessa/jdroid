package com.jdroid.android.debug;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.experiments.ExperimentHelper;
import com.jdroid.android.experiments.ExperimentHelper.Experiment;
import com.jdroid.android.experiments.ExperimentHelper.ExperimentVariant;
import com.jdroid.java.collections.Lists;

import java.util.List;
import java.util.Map.Entry;

public class ExperimentsDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_experimentsSettings;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		
		for (Entry<Experiment, ExperimentVariant> entry : ExperimentHelper.getExperimentsMap().entrySet()) {
			
			Experiment experiment = entry.getKey();
			ExperimentVariant experimentVariant = entry.getValue();
			
			ListPreference preference = new ListPreference(activity);
			preference.setKey(experiment.getId());
			preference.setTitle(activity.getString(R.string.jdroid_experimentTitle, experiment.getId()));
			preference.setDialogTitle(activity.getString(R.string.jdroid_experimentTitle, experiment.getId()));
			preference.setSummary(R.string.jdroid_experimentDescription);
			
			List<CharSequence> entries = Lists.newArrayList();
			for (ExperimentVariant each : experiment.getVariants()) {
				entries.add(each.getId());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preference.setDefaultValue(experimentVariant.getId());
			preference.setPersistent(false);
			preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					Experiment experiment = null;
					for (Experiment each : ExperimentHelper.getExperimentsMap().keySet()) {
						if (each.getId().equals(preference.getKey())) {
							experiment = each;
							break;
						}
					}
					
					Editor editor = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).edit();
					editor.putString(experiment.getId(), newValue.toString());
					editor.apply();
					
					((ListPreference)preference).setValue(newValue.toString());
					
					ExperimentHelper.registerExperiments(experiment);
					return false;
				}
			});
			preferenceGroup.addPreference(preference);
		}
	}
	
	@Override
	public Boolean isEnabled() {
		return !ExperimentHelper.getExperimentsMap().isEmpty();
	}
}
