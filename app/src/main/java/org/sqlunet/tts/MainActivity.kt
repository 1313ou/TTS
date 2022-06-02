package org.sqlunet.tts

import android.app.SearchManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.sqlunet.tts.databinding.ActivityMainBinding
import java.util.stream.Collectors

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

    @RequiresApi(Build.VERSION_CODES.N)
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
                    val text = voices.stream().map { v -> String.format("%s %s %s", v.name, v.locale, if (v.isNetworkConnectionRequired) "N" else "L") }.collect(Collectors.joining("\n"))
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
                    val text = engines.stream().map { v -> String.format("%s %s", v.label, v.name) }.collect(Collectors.joining("\n"))
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
}