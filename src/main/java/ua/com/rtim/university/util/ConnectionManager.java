package ua.com.rtim.university.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

	public Connection getConnection() throws SQLException {
		Properties properties = new Properties();
		try {
			URL fileUrl = getClass().getClassLoader().getResource("db.properties");
			if (fileUrl == null) {
				throw new FileNotFoundException("File " + "db.properties" + " not found");
			}
			try (FileReader propertyFile = new FileReader(fileUrl.getFile())) {
				properties.load(propertyFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String dbConnUrl = properties.getProperty("db.conn.url");
		String dbUserName = properties.getProperty("db.username");
		String dbPassword = properties.getProperty("db.password");
		return DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
	}
}
