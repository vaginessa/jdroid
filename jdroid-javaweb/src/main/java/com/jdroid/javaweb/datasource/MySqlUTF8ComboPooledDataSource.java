package com.jdroid.javaweb.datasource;


/**
 * {@link ComboPooledDataSource} that is set to use UTF-8 encoding for MySQL databases.
 * 
 * @author Estefanía Caravatti
 */
public class MySqlUTF8ComboPooledDataSource extends ComboPooledDataSource {
	
	/**
	 * @see com.jdroid.javaweb.datasource.ComboPooledDataSource#setJdbcUrl(java.lang.String)
	 */
	@Override
	public void setJdbcUrl(String jdbcUrl) {
		// Set default encoding as UTF-8. This works for MySQL only.
		super.setJdbcUrl(jdbcUrl + "?characterEncoding=UTF-8");
	}
}
