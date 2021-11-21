package ua.com.rtim.university.util;

import static java.nio.file.Files.lines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptRunner {

	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScriptRunner.class);

	public void generateDatabaseData(String scriptName) {
		try {
			URL fileUrl = getClass().getClassLoader().getResource(scriptName);
			if (fileUrl == null) {
				throw new FileNotFoundException("File '" + scriptName + "' not found");
			}
			File file = new File(fileUrl.getFile());
			try (Stream<String> fileLines = lines(file.toPath());
					Connection connection = connectionManager.getConnection();
					Statement statement = connection.createStatement()) {
				String scriptQuery = fileLines.collect(Collectors.joining(" "));
				statement.execute(scriptQuery);
				log.info("Tables will created!");
			}
		} catch (IOException | SQLException e) {
			log.error("Can't generate tabels", e);
		}
	}
}