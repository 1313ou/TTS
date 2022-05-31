package org.sqlunet.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Voices
{
	static public final String TAG = "__VOICE__";

	@FunctionalInterface
	public interface Consumer<T>
	{
		/**
		 * Performs this operation on the given argument.
		 *
		 * @param t the input argument
		 */
		void accept(T t);
	}

	private TextToSpeech tts;

	public void discoverVoices(final Context context, final Consumer<List<Voice>> consumer)
	{
		this.tts = new TextToSpeech(context, status -> {

			if (status != TextToSpeech.SUCCESS)
			{
				Log.e(TAG, "Init failed");
				return;
			}

			if (tts == null)
			{
				Log.e(TAG, "Null TTS");
				return;
			}

			// tts.getVoices().stream().sequential().sorted(Comparator.comparing(Voice::getName)).filter(v -> "en".equals(v.getLocale().getLanguage())).forEach(voice -> consumer.apply(voice));

			Set<Voice> allVoices = tts.getVoices();
			List<Voice> voices = new ArrayList<>();
			for (Voice voice : allVoices)
			{
				if ("en".equals(voice.getLocale().getLanguage()))
				{
					voices.add(voice);
				}
			}
			//Collections.sort(voices, Comparator.comparing(Voice::getName));
			Collections.sort(voices, (v1, v2) -> v1.getName().compareTo(v2.getName()));
			consumer.accept(voices);
		});
	}

	public void discoverVoice(final Context context, final Consumer<Voice> consumer)
	{
		this.tts = new TextToSpeech(context, status -> {

			if (status != TextToSpeech.SUCCESS)
			{
				Log.e(TAG, "Init failed");
				return;
			}

			if (tts == null)
			{
				Log.e(TAG, "Null TTS");
				return;
			}

			Voice voice = tts.getVoice();
			consumer.accept(voice);
		});
	}

	public void discoverLanguages(final Context context, final Consumer<List<Locale>> consumer)
	{
		this.tts = new TextToSpeech(context, status -> {

			if (status != TextToSpeech.SUCCESS)
			{
				Log.e(TAG, "Init failed");
				return;
			}

			if (tts == null)
			{
				Log.e(TAG, "Null TTS");
				return;
			}

			Set<Locale> allLocales = tts.getAvailableLanguages();
			List<Locale> locales = new ArrayList<>();
			for (Locale locale : allLocales)
			{
				if ("en".equals(locale.getLanguage()))
				{
					locales.add(locale);
				}
			}
			Collections.sort(locales, (l1, l2) -> l1.getCountry().compareTo(l2.getCountry()));
			consumer.accept(locales);
		});
	}
}