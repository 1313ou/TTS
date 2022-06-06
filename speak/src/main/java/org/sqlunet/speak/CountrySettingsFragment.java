package org.sqlunet.speak;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class CountrySettingsFragment extends PreferenceFragmentCompat
{
	public static final String COUNTRY_PREF = "country";
	public static final String COUNTRY_ENTRIES = "country_entries";
	public static final String COUNTRY_ENTRY_VALUES = "country_entry_values";

	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey)
	{
		this.setPreferencesFromResource(R.xml.country_preferences, rootKey);

		ListPreference pref = this.findPreference(COUNTRY_PREF);
		if (pref != null)
		{
			Bundle bundle = this.getArguments();
			if (bundle != null)
			{
				String[] entries = bundle.getStringArray(COUNTRY_ENTRIES);
				String[] entryValues = bundle.getStringArray(COUNTRY_ENTRY_VALUES);

				Context context = getContext();

				List<String> entries2 = entries == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(entries));
				entries2.add(context != null ? context.getString(R.string.none) : "None");
				pref.setEntries(entries2.toArray(new String[0]));

				List<String> entryValues2 = entryValues == null ? new ArrayList<>() :  new ArrayList<>(Arrays.asList(entryValues));
				entryValues2.add("");
				pref.setEntryValues(entryValues2.toArray(new String[0]));
			}
		}
	}
}