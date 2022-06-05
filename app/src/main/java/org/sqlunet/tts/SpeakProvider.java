/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.tts;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * FrameNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class SpeakProvider extends ContentProvider
{
	static private final String TAG = "SpeakProvider";

	// C O N T E N T   P R O V I D E R   A U T H O R I T Y

	static private final String AUTHORITY = "org.sqlunet.speak";
	private static final String VENDOR = "sqlunet_speak";
	private static final String SCHEME = "content://";

	// U R I M A T C H E R

	private static final int SUGGEST_WORDS = 10;
	static private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final String SEARCH_WORD_PATH = "suggest_word";
	private static final String URI = SEARCH_WORD_PATH + "/" + SearchManager.SUGGEST_URI_PATH_QUERY;
	private static final String TABLE = "words";

	static
	{
		matchURIs();
	}

	static private void matchURIs()
	{
		uriMatcher.addURI(AUTHORITY, URI + "/", SUGGEST_WORDS);
		uriMatcher.addURI(AUTHORITY, URI + "/*", SUGGEST_WORDS);
	}

	@NonNull
	static public String makeUri(final String uri)
	{
		return SCHEME + AUTHORITY + '/' + uri;
	}

	// D A T A B A S E

	private SQLiteDatabase db;

	// C O N S T R U C T O R

	@Override
	public boolean onCreate()
	{
		return false;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values)
	{
		return null;
	}

	@Override
	public int delete(@NonNull final Uri uri, @Nullable final String selection, @Nullable final String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public int update(@NonNull final Uri uri, @Nullable final ContentValues values, @Nullable final String selection, @Nullable final String[] selectionArgs)
	{
		return 0;
	}

	// M I M E

	@NonNull
	@Override
	public String getType(@NonNull final Uri uri)
	{
		if (uriMatcher.match(uri) == SUGGEST_WORDS)
		{
			return VENDOR + ".android.cursor.item/" + VENDOR + '.' + AUTHORITY + '.' + TABLE;
		}
		throw new UnsupportedOperationException("Illegal MIME type");
	}


	// Q U E R Y

	@Nullable
	@SuppressWarnings("boxing")
	@Override
	public Cursor query(@NonNull final Uri uri, final String[] projection0, final String selection0, final String[] selectionArgs0, String sortOrder0)
	{
		// choose the table to query and a sort order based on the code returned for the incoming URI
		final int code = uriMatcher.match(uri);
		Log.d(TAG, "URI " + String.format("%s (code %s)", uri, code));
		if (code == UriMatcher.NO_MATCH)
		{
			Log.e(TAG, "Malformed URI " + uri);
			throw new RuntimeException("Malformed URI " + uri);
		}

		String uriLast = uri.getLastPathSegment();
		if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(uriLast))
		{
			return null;
		}

		// database
		if (this.db == null)
		{
			Database dbh = new Database(getContext());
			this.db = dbh.getReadableDatabase();
		}

		// query

		final String[] projection = {"wordid AS _id", "word AS " + SearchManager.SUGGEST_COLUMN_TEXT_1, "word AS " + SearchManager.SUGGEST_COLUMN_QUERY};
		final String selection = "word LIKE ? || '%'";
		final String[] selectionArgs = {uriLast};

		{
			try
			{
				return db.query(TABLE, projection, selection, selectionArgs, null, null, null);
				// String query = String.format(QUERY, word);
				// return db.rawQuery(query, null)
			}
			catch (SQLException e)
			{
				Log.e(TAG, "Query exception", e);
				return null;
			}
		}
	}
}
