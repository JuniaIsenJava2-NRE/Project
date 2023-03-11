package isen.java.db;

import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;

public class DataSourceFactory {
    
    private static SQLiteDataSource dataSource;

	private DataSourceFactory() {
		// This is a static class that should not be instantiated.
		throw new IllegalStateException("This is a static class that should not be instantiated");
	}

    public static DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new SQLiteDataSource();
			dataSource.setUrl("jdbc:sqlite:sqlite.db");
		}
		return dataSource;
	}
}
