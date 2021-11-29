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

	private final String url;
	private final String userName;
	private final String password;

	public ConnectionManager() {
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
		this.url = properties.getProperty("db.conn.url");
		this.userName = properties.getProperty("db.username");
		this.password = properties.getProperty("db.password");
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}
}