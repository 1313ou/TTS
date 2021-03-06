package org.sqlunet.tts

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import org.sqlunet.speak.*

class ExamplesFragment : TextFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sb = SpannableStringBuilder()
        val p = sb.length
        sb.append("Examples\n")
        sb.setSpan(RelativeSizeSpan(1.2f), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(StyleSpan(Typeface.BOLD), p, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

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
            LexUnit("mom", "mɑm ", "US"),
            LexUnit("mom", "mɒm", "GB"),
            LexUnit("mom", "mʌm", "CA"),
            LexUnit("australian", "ɒˈstɹeɪ.liː.ən", "GB"),
            LexUnit("australian", "ɔˈstɹeɪ.li.ən", "US"),
            LexUnit("australian", "əˈstɹæɪ.ljən", "AU"),
            LexUnit("go", "ɡoʊ", "US"),
            LexUnit("go", "ɡɐʉ", "NZ"),
            LexUnit("go", "ɡəʉ", "AU"),
            LexUnit("go", "ɡəʊ", "GB"),
            LexUnit("understand", "(ˌ)ʌndəˈstænd", "GB"),
            LexUnit("understand", "ˌɞndəɹˈstand", "IE"),
            LexUnit("understand", "ˌʌndɚˈstænd", "US"),
            LexUnit("brother", "ˈbɹɐðɘ(ɹ)", "NZ"),
            LexUnit("brother", "ˈbɹʌðə(ɹ)", "GB"),
            LexUnit("brother", "ˈbɹʌðɚ", "US"),
            LexUnit("cafe", "kæˈfiː", "ZA"),
            LexUnit("cafe", "ˈkæfeɪ", "GB"),
            LexUnit("cafe", "ˌkæˈfeɪ", "US"),
        )

        lexunits.forEach { lexunit ->
            sb.append(lexunit.word)
            if (lexunit.variety != null) {
                sb.append(" [")
                sb.append(lexunit.variety)
                sb.append(']')
            }
            sb.append(' ')
            soundButton(sb, lexunit.ipa, lexunit) { lu ->
                Log.d("CLICK", "click <$lu>")
                val voice = Voices.findVoiceFor(lu.variety, requireContext())
                Log.d("PRONOUNCE", String.format("%s /%s/, country=%s, voice=%s", lu.word, lu.ipa, lu.variety, voice))
                TTS.pronounce(requireContext(), lu.word, lu.ipa, lu.variety, if (voice != null && voice.isNotEmpty()) voice else null)
            }
            sb.append('\n')

        }
        sb.append('\n')
        binding.text.movementMethod = LinkMovementMethod.getInstance()
        binding.text.text = sb
    }

    private fun <P> soundButton(sb: SpannableStringBuilder, caption: String, data: P?, consumer: Discover.Consumer<P>): CharSequence {

        VoiceButton.appendClickableImage(sb, org.sqlunet.speak.R.drawable.ic_speak_button, caption, { consumer.accept(data) }, requireContext())
        return sb
    }
}