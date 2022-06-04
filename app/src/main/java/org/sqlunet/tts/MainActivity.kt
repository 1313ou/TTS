package org.sqlunet.tts

import android.app.SearchManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.sqlunet.tts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("NAV", destination.displayName)
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // S E A R C H

    override fun onResume() {
        super.onResume()
        intent.getStringExtra(SearchManager.QUERY)?.let { Log.d("INTENT (resume)", it) }
        setIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(SearchManager.QUERY)?.let { Log.d("INTENT (new intent)", it) }
        setIntent(intent)
    }

    // M E N U

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_voices -> {
                Discover().discoverVoices(this) { voices ->
                    Log.d("VOICES", voices.toString())
                    //Toast.makeText(this, voices.toString(), Toast.LENGTH_LONG).show()
                    val text = voicesToText(voices)
                    val intent = Intent(this, TextActivity::class.java)
                    intent.putExtra("text", text)
                    startActivity(intent)
                }
                return true
            }
            R.id.action_voice -> {
                Discover().discoverVoice(this) { voice ->
                    Log.d("VOICE", voice.toString())
                    Toast.makeText(this, voice.toString(), Toast.LENGTH_LONG).show()
                }
                return true
            }
            R.id.action_languages -> {
                Discover().discoverLanguages(this) { languages ->
                    Log.d("LANGUAGES", languages.toString())
                    Toast.makeText(this, languages.toString(), Toast.LENGTH_LONG).show()
                }
                return true
            }
            R.id.action_engines -> {
                Discover().discoverEngines(this) { engines ->
                    Log.d("ENGINES", engines.toString())
                    val sb = enginesToText(engines)
                    val text = SpannableString.valueOf(sb)
                    val intent = Intent(this, TextActivity::class.java)
                    intent.putExtra("text", text)
                    startActivity(intent)
                }
                return true
            }
            R.id.action_engine -> {
                Discover().discoverEngine(this) { engine ->
                    Log.d("ENGINE", engine.toString())
                    Toast.makeText(this, engine, Toast.LENGTH_LONG).show()

                    val intent = Intent(this, TextActivity::class.java)
                    startActivity(intent)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // N A V

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // F O R M A T


    private fun voicesToText(voices: List<Voice>): CharSequence {
        val sb = SpannableStringBuilder()
        voices.forEach { v ->
            var p = sb.length
            sb.append(v.name)
            sb.setSpan(RelativeSizeSpan(1.2f), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(StyleSpan(Typeface.BOLD), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(ForegroundColorSpan(Color.BLUE), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.append(' ')
            p = sb.length
            sb.append(v.locale.toString())
            sb.setSpan(ForegroundColorSpan(Color.MAGENTA), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.append(' ')
            sb.append(if (v.isNetworkConnectionRequired) "network" else "local")
            sb.append('\n')
        }
        return sb
    }

    private fun enginesToText(engines: List<TextToSpeech.EngineInfo>): CharSequence {
        val sb = SpannableStringBuilder()
        engines.forEach { e ->
            var p = sb.length
            sb.append(e.label)
            sb.setSpan(RelativeSizeSpan(1.2f), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(StyleSpan(Typeface.BOLD), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.append(' ')
            p = sb.length
            sb.append(e.name)
            sb.setSpan(ForegroundColorSpan(Color.MAGENTA), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.append('\n')
        }
        return sb
    }
}