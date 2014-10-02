package com.jdroid.android.debug;

import java.util.Date;
import android.content.ContentValues;
import android.database.Cursor;
import com.jdroid.android.repository.sqlite.SQLiteRepository;
import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.SQLiteHelper;

public class DebugLogsRepository extends SQLiteRepository<DebugLog> {
	
	public DebugLogsRepository(SQLiteHelper dbHelper) {
		super(dbHelper);
	}
	
	/**
	 * @see com.jdroid.android.repository.sqlite.SQLiteRepository#getTableName()
	 */
	@Override
	protected String getTableName() {
		return "DebugLog";
	}
	
	/**
	 * @see com.jdroid.android.repository.sqlite.SQLiteRepository#getColumns()
	 */
	@Override
	protected Column[] getColumns() {
		return DebugLogsColumns.values();
	}
	
	/**
	 * @see com.jdroid.android.repository.sqlite.SQLiteRepository#createObjectFromCursor(android.database.Cursor)
	 */
	@Override
	protected DebugLog createObjectFromCursor(Cursor cursor) {
		DebugLog log = new DebugLog();
		log.setId((Long)DebugLogsColumns.ID.readValue(cursor));
		log.setText(DebugLogsColumns.TEXT.readValue(cursor).toString());
		log.setDateTime((Date)DebugLogsColumns.DATE_TIME.readValue(cursor));
		return log;
	}
	
	/**
	 * @see com.jdroid.android.repository.sqlite.SQLiteRepository#createContentValuesFromObject(com.jdroid.android.domain.Entity)
	 */
	@Override
	protected ContentValues createContentValuesFromObject(DebugLog item) {
		ContentValues values = new ContentValues();
		DebugLogsColumns.ID.addValue(values, item.getId());
		DebugLogsColumns.TEXT.addValue(values, item.getText());
		DebugLogsColumns.DATE_TIME.addValue(values, item.getDateTime());
		return values;
	}
	
}
