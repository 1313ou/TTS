package org.sqlunet.tts

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class VoiceFragment : PreferenceFragmentCompat() {

    companion object {

        const val VOICE_PREF = "voice"
        const val VOICE_ENTRIES = "voice_entries"
        const val VOICE_ENTRY_VALUES = "voice_entry_values"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.voice_preferences, rootKey)

        val entries = arguments?.getStringArray(VOICE_ENTRIES)
        val entryValues = arguments?.getStringArray(VOICE_ENTRY_VALUES)
        val pref = findPreference<ListPreference>(VOICE_PREF)
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