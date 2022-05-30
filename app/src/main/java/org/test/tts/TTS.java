package org.test.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class TTS
{
	static public final String TAG = "__TTS__";

	static public final Locale DEFAULT_LOCALE = Locale.UK;

	static private Locale toLocale(final String locale)
	{
		switch(locale)
		{
			case "GB":
				return Locale.UK;
			case "US":
				return Locale.US;
			case "CA":
				return Locale.CANADA;
			case "AU":
				return new Locale("en","AU");
			case "IE":
				return new Locale("en","IE");
			case "NZ":
				return new Locale("en", "NZ");
			case "ZA":
				return new Locale("en", "ZA");
			case "SG":
				return new Locale("en", "SG");
			default:
				return DEFAULT_LOCALE;
		}
	}

	public static void pronounce(Context context, String word, String ipa, String locale)
	{
		new TTS(context, word, ipa, toLocale(locale));
	}

	private TextToSpeech tts;

	public TTS(final Context context, final String written, final String ipa, final Locale locale)
	{
		this.tts = new TextToSpeech(context, status -> {

			if (status != TextToSpeech.SUCCESS)
			{
				Toast.makeText(context, "Init failed", Toast.LENGTH_SHORT).show();
				return;
			}

			int result = tts.setLanguage(locale);
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show();
				return;
			}
			if (tts == null)
			{
				Toast.makeText(context, "Null TTS", Toast.LENGTH_SHORT).show();
				return;
			}
			final String phoneme = String.format("<phoneme alphabet='IPA' ph='%s'>%s</phoneme>", ipa, written);
			final String text = String.format("<speak xml:lang='%s'>%s.</speak>", locale, phoneme);
			tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
			{
				@Override
				public void onStart(final String s)
				{
					Log.d(TAG, "start " + s);
				}

				@Override
				public void onDone(final String s)
				{
					Log.d(TAG, "done " + s);
					tts.shutdown();
				}

				@Override
				public void onError(final String s)
				{
					Log.d(TAG, "error " + s);
				}
			});
			Log.d(TAG, "run " + written + " " + text);
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, written);
		});
	}
}
