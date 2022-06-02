package org.sqlunet.tts

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            Discover().discoverVoices(this) { voices ->

                run {
                    val voicesNames: Array<String?> = voices.stream().map { v -> v.name }.toArray { size -> arrayOfNulls<String>(size) }
                    val voicesInfos: Array<String?> = voices.stream().map { v -> v.name + " " + if (v.isNetworkConnectionRequired) "N" else "L" }.toArray { size -> arrayOfNulls<String>(size) }
                    val args = Bundle()
                    args.putStringArray("entryValues", voicesNames)
                    args.putStringArray("entries", voicesInfos)
                    val f = SettingsFragment()
                    f.arguments = args
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.settings, f)
                        .commit()
                }
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

            setPreferencesFromResource(R.xml.voice_preferences, rootKey)
            val entries = arguments?.getStringArray("entries")
            val entryValues = arguments?.getStringArray("entryValues")
            val pref = findPreference<ListPreference>("voice")
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
}
