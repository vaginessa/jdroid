package com.jdroid.android.debug;

import java.util.List;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.analytics.ExperimentHelper.Experiment;
import com.jdroid.android.analytics.ExperimentHelper.ExperimentVariant;
import com.jdroid.java.collections.Lists;

public class DebugExperimentsHelper {
	
	public static void initPreferences(final Activity activity, PreferenceScreen preferenceScreen) {
		
		if (!ExperimentHelper.getExperimentsMap().isEmpty()) {
			
			PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
			preferenceCategory.setTitle(R.string.experimentsSettings);
			preferenceScreen.addPreference(preferenceCategory);
			for (Entry<Experiment, ExperimentVariant> entry : ExperimentHelper.getExperimentsMap().entrySet()) {
				
				Experiment experiment = entry.getKey();
				ExperimentVariant experimentVariant = entry.getValue();
				
				ListPreference preference = new ListPreference(activity);
				preference.setKey(experiment.getId());
				preference.setTitle(activity.getString(R.string.experimentTitle, experiment.getId()));
				preference.setDialogTitle(activity.getString(R.string.experimentTitle, experiment.getId()));
				preference.setSummary(R.string.experimentDescription);
				
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
						editor.commit();
						
						((ListPreference)preference).setValue(newValue.toString());
						
						ExperimentHelper.registerExperiment(experiment);
						return false;
					}
				});
				preferenceCategory.addPreference(preference);
			}
		}
	}
}
