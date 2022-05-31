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
            Voices().discoverVoices(this) { voices ->
                run {

                    val voicesNames: Array<String?> = voices.stream().map { v -> v.name }.toArray { size -> arrayOfNulls<String>(size) }
                    val voicesInfos: Array<String?> = voices.stream().map { v -> v.name + " " + v.isNetworkConnectionRequired }.toArray { size -> arrayOfNulls<String>(size) }
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

    class SettingsFragment0 : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
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
                pref.entries = entries
                pref.entryValues = entryValues
            }
        }
    }
}
