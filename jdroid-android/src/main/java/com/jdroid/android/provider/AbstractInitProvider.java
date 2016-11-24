package com.jdroid.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public abstract class AbstractInitProvider extends ContentProvider {

	protected abstract void init();

	@Override
	public final boolean onCreate() {
		init();
		return false;
	}

	@Nullable
	@Override
	public final Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
		return null;
	}

	@Nullable
	@Override
	public final String getType(Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public final Uri insert(Uri uri, ContentValues contentValues) {
		return null;
	}

	@Override
	public final int delete(Uri uri, String s, String[] strings) {
		return 0;
	}

	@Override
	public final int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
		return 0;
	}
}
