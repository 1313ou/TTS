package org.sqlunet.tts;

import androidx.annotation.NonNull;

public class Pronunciation
{
	public final String ipa;

	public final String variety;

	public Pronunciation(final String value, final String variety)
	{
		this.ipa = value;
		this.variety = variety;
	}

	public boolean hasVariety()
	{
		return variety != null;
	}

	@NonNull
	@Override
	public String toString()
	{
		return variety == null ? ipa : String.format("[%s] %s", variety, ipa);
	}
}
