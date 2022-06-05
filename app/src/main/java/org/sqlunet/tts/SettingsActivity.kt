package org.sqlunet.tts

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.sqlunet.speak.Discover

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
                    args.putStringArray(VoiceSettingsFragment.VOICE_ENTRY_VALUES, voicesNames)
                    args.putStringArray(VoiceSettingsFragment.VOICE_ENTRIES, voicesInfos)
                    val f = VoiceSettingsFragment()
                    f.arguments = args
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.voice_settings, f)
                        .commit()
                }
            }
            Discover().discoverLanguages(this) { languages ->
                run {
                    val languagesNames: Array<String?> = languages.stream().map { l -> l.toString() }.toArray { size -> arrayOfNulls<String>(size) }
                    val languagesInfos: Array<String?> = languages.stream().map { l -> l.country }.toArray { size -> arrayOfNulls<String>(size) }
                    val args = Bundle()
                    args.putStringArray(CountrySettingsFragment.COUNTRY_ENTRY_VALUES, languagesNames)
                    args.putStringArray(CountrySettingsFragment.COUNTRY_ENTRIES, languagesInfos)
                    val f = CountrySettingsFragment()
                    f.arguments = args
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.country_settings, f)
                        .commit()
                }
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
