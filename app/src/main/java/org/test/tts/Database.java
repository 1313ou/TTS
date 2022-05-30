package org.test.tts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Database extends SQLiteAssetHelper
{
	public static class Pronunciation
	{
		public final String value;

		public final String variety;

		public Pronunciation(final String value, final String variety)
		{
			this.value = value;
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
			return variety == null ? value : String.format("[%s] %s", variety, value);
		}
	}

	private static final String DATABASE_NAME = "sqlunet-speak.db";

	private static final int DATABASE_VERSION = 1;

	public Database(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String QUERY = //
			"SELECT variety,pronunciation " + //
					"FROM lexes_pronunciations " + //
					"INNER join lexes USING (luid) " + //
					"INNER join words USING (wordid) " + //
					"INNER join pronunciations USING (pronunciationid) " + //
					"WHERE word = '%s' " + //
					"GROUP BY word,variety,pronunciation " + //
					"ORDER BY count(*) DESC, variety;";

	public static List<Pronunciation> query(String word, Context context)
	{
		List<Pronunciation> results = new ArrayList<>();
		Database dbh = new Database(context);
		try (SQLiteDatabase db = dbh.getReadableDatabase())
		{
			String query = String.format(QUERY, word);
			try (Cursor cursor = db.rawQuery(query, null))
			{
				if (cursor.moveToFirst())
				{
					do
					{
						String variety = cursor.getString(0);
						String value = cursor.getString(1);
						Pronunciation result = new Pronunciation(value, variety);
						results.add(result);
						Log.d("DB", result.toString());
					}
					while (cursor.moveToNext());
				}
			}
		}
		return results;
	}
}