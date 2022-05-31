package org.sqlunet.tts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import org.sqlunet.tts.databinding.FragmentPronounceBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PronounceFragment : Fragment() {

    private var _binding: FragmentPronounceBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPronounceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.word.requestFocus()
        binding.word.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val w = binding.word.query.toString()
                Log.d("QUERY", w)
                val pronunciations = Database.query(w, requireContext());
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pronunciations)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.pronunciations.adapter = adapter
                return true
            }
        })
        binding.pronunciations.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.locales.visibility = View.VISIBLE
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position) as Database.Pronunciation
                Log.d("SELECT", selected.toString())
                binding.locales.visibility = if(selected.hasVariety()) View.INVISIBLE else View.VISIBLE
            }
        }
        binding.pronounce.setOnClickListener {

                val w = binding.word.query.toString()
                val p = binding.pronunciations.selectedItem as Database.Pronunciation
                val v = p.value
                val l1 = p.variety
                val l0 = binding.locales.selectedItem.toString()
                val l = l1 ?: l0
                val s = String.format("%s /%s/ %s", w, v, l)
                Log.d("PRONOUNCE", s)
            TTS.pronounce(requireContext(), w, v, l)
                /*
                Snackbar.make(v, s, Snackbar.LENGTH_LONG)
                    .setAnchorView(this.view)
                    .setAction(R.string.pronounce) {
                        TTS.pronounce(requireContext(), w, p, l)
                    }.show()
                 */
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}