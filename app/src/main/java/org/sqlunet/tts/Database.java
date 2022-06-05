package org.sqlunet.tts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.sqlunet.speak.Pronunciation;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper
{

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