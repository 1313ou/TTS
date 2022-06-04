package org.sqlunet.tts

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.sqlunet.tts.databinding.FragmentTextBinding

class TextFragment : Fragment() {

    private var _binding: FragmentTextBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = activity?.intent?.getCharSequenceExtra("text")
        if (text != null) {
            binding.text.setText(text)
        } else {
            val sb = SpannableStringBuilder()
            sb.append("Examples\n")

            val lexunits = arrayOf(
                LexUnit("agnostic", "aɡˈnɒstɪk", "GB"),
                LexUnit("mobile", "ˈmoʊbil", "US"),
                LexUnit("mobile", "ˈməʊbaɪl", "GB"),
                LexUnit("bass", "beɪs", null),
                LexUnit("bass", "bæs", null),
                LexUnit("row", "ɹaʊ", null),
                LexUnit("row", "ɹəʊ", null),
                LexUnit("wind", "waɪnd", null),
                LexUnit("wind", "ˈwɪnd", null),
            )

            lexunits.forEach { lexunit ->
                sb.append(lexunit.word)
                soundButton(sb, lexunit.ipa, lexunit) { lu ->
                    Log.d("CLICK", "click <" + lu + ">")
                    TTS.pronounce(requireContext(), lu.word, lu.ipa, lu.variety, null)
                }
                sb.append('\n')
            }
            binding.text.setMovementMethod(LinkMovementMethod.getInstance())
            binding.text.setText(sb)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <P> soundButton(sb: SpannableStringBuilder, caption: String, data: P?, consumer: Discover.Consumer<P>): CharSequence {

        VoiceButton.appendClickableImageRes(sb, R.drawable.ic_speak3, caption, { consumer.accept(data) }, requireContext())
        return sb
    }
}