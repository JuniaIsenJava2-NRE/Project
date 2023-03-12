package isen.java.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;

public class DatabaseExporterTestCase {
    
    @Before
    public void initDb() throws SQLException {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();

		statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (\r\n"
				+ "idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n"
				+ "lastname VARCHAR(45) NOT NULL,\r\n" + "firstname VARCHAR(45) NOT NULL,\r\n"
				+ "nickname VARCHAR(45) NOT NULL,\r\n" + "phone_number VARCHAR(15) NULL,\r\n"
				+ "address VARCHAR(200) NULL,\r\n" + "email_address VARCHAR(150) NULL,\r\n"
                + "birth_date DATE NULL);");
                
        statement.close();
        connection.close();
    }

    @Test
    public void shouldExportDatabase() {
        DatabaseExporter.exportDatabase(Paths.get("people"));
    }
}
