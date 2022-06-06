package org.sqlunet.speak;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class VoiceSettingsFragment extends PreferenceFragmentCompat
{
	public static final String VOICE_PREF = "voice";
	public static final String VOICE_ENTRIES = "voice_entries";
	public static final String VOICE_ENTRY_VALUES = "voice_entry_values";

	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey)
	{
		this.setPreferencesFromResource(R.xml.voice_preferences, rootKey);

		MultiSelectListPreference pref = this.findPreference(VOICE_PREF);
		if (pref != null)
		{
			pref.setSummaryProvider(preference -> prepareSummary(pref));

			Bundle bundle = this.getArguments();
			if (bundle != null)
			{
				String[] entries = bundle.getStringArray(VOICE_ENTRIES);
				String[] entryValues = bundle.getStringArray(VOICE_ENTRY_VALUES);

				Context context = getContext();

				List<String> entries2 = entries == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(entries));
				entries2.add(context != null ? context.getString(R.string.none) : "None");
				pref.setEntries(entries2.toArray(new String[0]));

				List<String> entryValues2 = entryValues == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(entryValues));
				entryValues2.add("");
				pref.setEntryValues(entryValues2.toArray(new String[0]));
			}
		}
	}

	private CharSequence prepareSummary(MultiSelectListPreference pref)
	{
		List<String> titles = new ArrayList<>();
		CharSequence[] entryValues = pref.getEntryValues();
		Set<String> persisted = pref.getPersistedStringSet(null);
		if (persisted != null)
		{
			int i = 0;
			for (CharSequence value : entryValues)
			{
				if (persisted.contains(value.toString()))
				{
					titles.add(entryValues[i].toString().substring(3));
				}
				i++;
			}
			return String.join("\n", titles);
		}
		return "";
	}
}
