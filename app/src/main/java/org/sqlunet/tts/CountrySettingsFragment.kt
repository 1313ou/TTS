package org.sqlunet.tts

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class CountrySettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val COUNTRY_PREF = "country"
        const val COUNTRY_ENTRIES = "country_entries"
        const val COUNTRY_ENTRY_VALUES = "country_entry_values"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.country_preferences, rootKey)

        val entries = arguments?.getStringArray(COUNTRY_ENTRIES)
        val entryValues = arguments?.getStringArray(COUNTRY_ENTRY_VALUES)
        val pref = findPreference<ListPreference>(COUNTRY_PREF)
        if (pref != null) {
            val entries2: MutableList<String?> = entries?.toMutableList() ?: mutableListOf()
            val entryValues2: MutableList<String?> = entryValues?.toMutableList() ?: mutableListOf()
            entries2.add("None")
            entryValues2.add("")
            pref.entries = entries2.toTypedArray()
            pref.entryValues = entryValues2.toTypedArray()
        }
    }
}