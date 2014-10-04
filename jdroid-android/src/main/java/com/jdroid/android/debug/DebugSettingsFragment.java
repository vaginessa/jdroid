package com.jdroid.android.debug;

import java.util.List;
import java.util.Map.Entry;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.analytics.ExperimentHelper.Experiment;
import com.jdroid.android.analytics.ExperimentHelper.ExperimentVariant;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.CollectionUtils;

public class DebugSettingsFragment extends AbstractPreferenceFragment {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(getDebugPreferences());
	}
	
	/**
	 * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initExperiments();
		initDebugGcmMessages();
		DebugLogHelper.initPreferences(getActivity(), getPreferenceScreen());
		
		ListView listView = ((ListView)findView(android.R.id.list));
		
		View debugCrashView = new DebugCrashView(getActivity());
		listView.addFooterView(debugCrashView);
		
		View debugImageLoaderView = new DebugImageLoaderView(getActivity());
		listView.addFooterView(debugImageLoaderView);
		
		View debugInfoView = new DebugInfoView(getActivity());
		listView.addFooterView(debugInfoView);
		
		View customDebugInfoView = getCustomDebugInfoView();
		if (customDebugInfoView != null) {
			listView.addFooterView(debugInfoView);
		}
	}
	
	protected void initExperiments() {
		if (!ExperimentHelper.getExperimentsMap().isEmpty()) {
			
			PreferenceCategory preferenceCategory = new PreferenceCategory(getActivity());
			preferenceCategory.setTitle(R.string.experimentsSettings);
			getPreferenceScreen().addPreference(preferenceCategory);
			for (Entry<Experiment, ExperimentVariant> entry : ExperimentHelper.getExperimentsMap().entrySet()) {
				
				Experiment experiment = entry.getKey();
				ExperimentVariant experimentVariant = entry.getValue();
				
				ListPreference preference = new ListPreference(getActivity());
				preference.setKey(experiment.getId());
				preference.setTitle(getString(R.string.experimentTitle, experiment.getId()));
				preference.setDialogTitle(getString(R.string.experimentTitle, experiment.getId()));
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
	
	protected void initDebugGcmMessages() {
		
		final List<? extends GcmMessage> gcmMessages = getGcmMessages();
		if (CollectionUtils.isNotEmpty(gcmMessages)) {
			PreferenceCategory preferenceCategory = new PreferenceCategory(getActivity());
			preferenceCategory.setTitle(R.string.gcmSettings);
			getPreferenceScreen().addPreference(preferenceCategory);
			
			ListPreference preference = new ListPreference(getActivity());
			preference.setTitle(R.string.emulateGcmMessageTitle);
			preference.setDialogTitle(R.string.emulateGcmMessageTitle);
			preference.setSummary(R.string.emulateGcmMessageDescription);
			List<CharSequence> entries = Lists.newArrayList();
			for (GcmMessage entry : gcmMessages) {
				entries.add(entry.getMessageKey());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preference.setPersistent(false);
			preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					
					GcmMessage gcmMessage = null;
					for (GcmMessage each : gcmMessages) {
						if (each.getMessageKey().equals(newValue.toString())) {
							gcmMessage = each;
							break;
						}
					}
					
					Intent intent = getEmulatedGcmMessageIntent(gcmMessage);
					if (intent != null) {
						gcmMessage.handle(intent);
					}
					
					return false;
				}
			});
			preferenceCategory.addPreference(preference);
		}
	}
	
	protected Intent getEmulatedGcmMessageIntent(GcmMessage gcmMessage) {
		return null;
	}
	
	protected List<? extends GcmMessage> getGcmMessages() {
		return null;
	}
	
	protected Integer getDebugPreferences() {
		return R.xml.debug_preferences;
	}
	
	protected View getCustomDebugInfoView() {
		return null;
	}
}