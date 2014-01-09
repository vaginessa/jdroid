package com.jdroid.android.sqlite;

import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import com.jdroid.android.domain.Entity;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.ReflectionUtils;

/**
 * 
 * @author Maxi Rosson
 * @param <T>
 */
public abstract class AbstractDbAdapter<T> {
	
	public enum CommonColumn implements Column<Entity> {
		ID("id", DataType.INTEGER, "primary key autoincrement") {
			
			@Override
			public void addValue(ContentValues values, Entity item) {
				values.put(getColumnName(), item.getId());
			};
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> T readValue(Cursor cursor) {
				return (T)Long.valueOf(cursor.getLong(cursor.getColumnIndex(getColumnName())));
			}
		};
		
		private String columnName;
		private DataType dataType;
		private String extraQualifier;
		private Boolean optional;
		
		private CommonColumn(String columnName, DataType dataType, String extraQualifier) {
			this.columnName = columnName;
			this.dataType = dataType;
			this.extraQualifier = extraQualifier;
			optional = null;
		}
		
		/**
		 * @see com.jdroid.android.sqlite.Column#getDataType()
		 */
		@Override
		public DataType getDataType() {
			return dataType;
		}
		
		/**
		 * @see com.jdroid.android.sqlite.Column#getColumnName()
		 */
		@Override
		public String getColumnName() {
			return columnName;
		}
		
		/**
		 * @see com.jdroid.android.sqlite.Column#isOptional()
		 */
		@Override
		public Boolean isOptional() {
			return optional;
		}
		
		/**
		 * @see com.jdroid.android.sqlite.Column#getExtraQualifier()
		 */
		@Override
		public String getExtraQualifier() {
			return extraQualifier;
		}
	}
	
	private DatabaseHelper databaseHelper;
	
	/**
	 * Open the database. If it cannot be opened, try to create a new instance of the database. If it cannot be created,
	 * throw an exception to signal the failure
	 * 
	 * @throws SQLException if the database could be neither opened or created
	 */
	public AbstractDbAdapter() throws SQLException {
		databaseHelper = DatabaseHelper.get();
		databaseHelper.addCreateSQL(getCreateTableSQL());
		databaseHelper.addDropSQL("DROP TABLE IF EXISTS " + getTableName());
	}
	
	public void close() {
		databaseHelper.close();
	}
	
	protected void insert(ContentValues values, Entity entity) {
		long rowId = databaseHelper.getWritableDatabase().insertOrThrow(getTableName(), null, values);
		if (rowId != -1) {
			ReflectionUtils.setId(entity, rowId);
		} else {
			throw new SQLException("Error when inserting " + entity);
		}
	}
	
	protected void delete(Entity entity) {
		int rows = databaseHelper.getWritableDatabase().delete(getTableName(),
			CommonColumn.ID.getColumnName() + "=" + entity.getId(), null);
		if (rows != 1) {
			throw new SQLException("Error when deleting " + entity);
		}
	}
	
	protected Cursor fetchAll(String[] columns) {
		return databaseHelper.getWritableDatabase().query(getTableName(), columns, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor over the list of all the {@link Entity}s in the database
	 * 
	 * @return Cursor over all {@link Entity}s
	 */
	public Cursor fetchAll() {
		return fetchAll(getColumnsNames());
	}
	
	/**
	 * Return a Cursor positioned at the {@link Entity} that matches the given rowId
	 * 
	 * @param rowId id of the {@link Entity} to retrieve
	 * @return Cursor positioned to matching {@link Entity}, if found
	 * @throws SQLException if note could not be found/retrieved
	 */
	public Cursor fetch(long rowId) throws SQLException {
		return fetch(rowId, getColumnsNames());
	}
	
	private String[] getColumnsNames() {
		List<String> columns = Lists.newArrayList();
		for (Column<?> column : getColumns()) {
			columns.add(column.getColumnName());
		}
		return columns.toArray(new String[] {});
	}
	
	public Boolean isEmpty() {
		Cursor cursor = fetchAll(null);
		boolean isEmpty = cursor.getCount() == 0;
		cursor.close();
		return isEmpty;
	}
	
	protected Cursor fetch(long rowId, String[] columns) throws SQLException {
		Cursor mCursor = databaseHelper.getWritableDatabase().query(true, getTableName(), columns,
			CommonColumn.ID.getColumnName() + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	protected void update(Entity entity, ContentValues values) {
		long rows = databaseHelper.getWritableDatabase().update(getTableName(), values,
			CommonColumn.ID.getColumnName() + "=" + entity.getId(), null);
		if (rows <= 0) {
			throw new SQLException("Error when updating " + entity);
		}
	}
	
	protected abstract String getTableName();
	
	private String getCreateTableSQL() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Column<?> column : getColumns()) {
			if (!first) {
				builder.append(", ");
			}
			first = false;
			addColumn(builder, column);
		}
		return getCreateTableSQL(builder.toString());
	}
	
	protected abstract List<Column<?>> getColumns();
	
	protected String getCreateTableSQL(String columns) {
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(getTableName());
		builder.append("(");
		builder.append(columns);
		builder.append(");");
		return builder.toString();
	}
	
	protected void addColumn(StringBuilder builder, Column<?> column) {
		builder.append(column.getColumnName());
		builder.append(" ");
		builder.append(column.getDataType().getType());
		Boolean optional = column.isOptional();
		if (optional != null) {
			builder.append(optional ? " null " : " not null ");
		}
		String extraQualifier = column.getExtraQualifier();
		if (extraQualifier != null) {
			builder.append(" ");
			builder.append(extraQualifier);
			builder.append(" ");
		}
	}
}
