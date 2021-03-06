package org.sqlunet.tts

import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.sqlunet.speak.Pronunciation
import org.sqlunet.speak.TTS
import org.sqlunet.speak.Voices
import org.sqlunet.tts.databinding.FragmentPronounceBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PronounceFragment : Fragment() {

    private var _binding: FragmentPronounceBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        requireActivity().intent?.let { it ->
            Log.d("INTENT", "in fragment $it")
            it.getStringExtra(SearchManager.QUERY)?.let { Log.d("INTENT", it) }
            handleSearchIntent(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPronounceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.word.requestFocus()
        binding.word.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                binding.pronunciations.adapter = null
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val w = binding.word.query.toString()
                Log.d("QUERY", w)
                val pronunciations = Database.query(w, requireContext())
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pronunciations)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.pronunciations.adapter = adapter
                return true
            }
        })
        binding.pronunciations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.locales.visibility = View.VISIBLE
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position) as Pronunciation
                Log.d("SELECT", selected.toString())
                binding.locales.visibility = if (selected.hasVariety()) View.INVISIBLE else View.VISIBLE
            }
        }
        binding.pronounce.setOnClickListener {

            val word = binding.word.query.toString()
            val p = binding.pronunciations.selectedItem as Pronunciation?
            if (p != null) {
                val pronunciation = p.ipa
                val l1 = p.variety
                val l0 = binding.locales.selectedItem.toString()
                val lang = l1 ?: l0
                val voice = Voices.findVoiceFor(lang, requireContext())
                Log.d("PRONOUNCE", String.format("%s /%s/, %s, %s", word, pronunciation, lang, voice))
                TTS.pronounce(requireContext(), word, pronunciation, lang, if (voice != null && voice.isNotEmpty()) voice else null)
                this.view?.let { it1 ->
                    Snackbar.make(it1, String.format("%s /%s/, %s, %s", word, pronunciation, lang, voice), Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.word)
                        //.setAction(R.string.pronounce) {
                        //    TTS.pronounce(requireContext(), w, p, l)
                        //}
                        .show()
                }
            }
        }

        // search info
        val componentName: ComponentName? = activity?.componentName
        val searchManager = activity?.getSystemService(AppCompatActivity.SEARCH_SERVICE) as SearchManager
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        binding.word.setSearchableInfo(searchableInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleSearchIntent(intent: Intent?) {
        val action = intent?.action
        val isActionView = Intent.ACTION_VIEW == action
        if (isActionView || Intent.ACTION_SEARCH == action) {
            // search query submit (SEARCH) or suggestion selection (when a suggested item is selected) (VIEW)
            val query = intent?.getStringExtra(SearchManager.QUERY)
            if (query != null) {
                if (isActionView) {
                    binding.word.clearFocus()
                    binding.word.isFocusable = false
                    binding.word.setQuery("", false)
                    binding.word.isIconified = true
                }
                binding.word.setQuery(query, true)
            }
        }
    }
}