package org.sqlunet.tts;

import android.content.Context;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

import androidx.annotation.Nullable;

public class TTS
{
	static public final String TAG = "__TTS__";

	static public final Locale DEFAULT_LOCALE = Locale.UK;

	static private Locale toLocale(final String locale)
	{
		if (locale == null)
		{
			return DEFAULT_LOCALE;
		}
		switch (locale)
		{
			case "GB":
				return Locale.UK;
			case "US":
				return Locale.US;
			case "CA":
				return Locale.CANADA;
			case "AU":
				return new Locale("en", "AU");
			case "IE":
				return new Locale("en", "IE");
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

	public static void pronounce(final Context context, final String word, final String ipa, final String locale, final String voiceName)
	{
		new TTS(context, word, ipa, toLocale(locale), voiceName);
	}

	private TextToSpeech tts;

	public TTS(final Context context, final String written, final String ipa, final Locale locale, final String voiceName)
	{
		this.tts = new TextToSpeech(context, status -> {

			if (status != TextToSpeech.SUCCESS)
			{
				Toast.makeText(context, "Init failed", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "Init failed");
				return;
			}
			Log.d(TAG, "Init succeeded");

			if (locale != null)
			{
				int result = tts.setLanguage(locale);
				if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
				{
					Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show();
					Log.e(TAG, "Set language failed " + locale);
					return;
				}
			}
			if (voiceName != null)
			{
				Voice voice;
				try
				{
					voice = getVoice(voiceName);
					if (voice != null)
					{
						if (locale != null && voice.getLocale().getCountry().equals(locale.getCountry()))
						{
							Log.e(TAG, "Set voice " + voiceName);
							int result = tts.setVoice(voice);
							if (result != TextToSpeech.SUCCESS)
							{
								Toast.makeText(context, "Voice " + voiceName + " failed", Toast.LENGTH_SHORT).show();
								Log.e(TAG, "Error voice " + voiceName + " failed");
								return;
							}
						}
					}
					else
					{
						Toast.makeText(context, "Voice " + voiceName + " not found", Toast.LENGTH_SHORT).show();
						Log.e(TAG, "Error voice " + voiceName + " not found");
						return;
					}
				}
				catch (RemoteException e)
				{
					Toast.makeText(context, "Voice " + voiceName + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
					Log.e(TAG, "Error voice " + voiceName + " " + e.getMessage());
					return;
				}
			}
			if (tts == null)
			{
				Toast.makeText(context, "Null TTS", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "Null TTS");
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
					Log.e(TAG, "error " + s);
				}
			});
			Log.d(TAG, "pronounce " + written + " " + ipa + " " + '"' + text + '"');
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, written);
		});
	}

	private Voice getVoice(String voiceName) throws RemoteException
	{
		Set<Voice> voices = tts.getVoices();
		if (voices == null)
		{
			Log.w(TAG, "getVoices returned null");
			return null;
		}
		for (Voice voice : voices)
		{
			if (voice.getName().equals(voiceName))
			{
				return voice;
			}
		}
		Log.w(TAG, "Could not find voice " + voiceName + " in voice list");
		return null;
	}
}
