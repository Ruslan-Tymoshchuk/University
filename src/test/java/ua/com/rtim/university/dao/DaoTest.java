package ua.com.rtim.university.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import ua.com.rtim.university.util.ConnectionManager;
import ua.com.rtim.university.util.ScriptRunner;

public class DaoTest {

	protected ConnectionManager connectionManager = new ConnectionManager();
	protected ScriptRunner scriptRunner = new ScriptRunner();
	protected GroupDao groupDao = new GroupDao(connectionManager);
	protected StudentDao studentDao = new StudentDao(connectionManager, groupDao);
	protected CourseDao courseDao = new CourseDao(connectionManager, studentDao);

	protected IDatabaseConnection getConnection() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
		IDataSet dataSet = getDataSet("actualdata.xml");
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		return connection;
	}

	protected IDataSet getDataSet(String datasetName) throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream(datasetName));
	}
}