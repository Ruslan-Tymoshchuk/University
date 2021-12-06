package ua.com.rtim.university.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import ua.com.rtim.university.util.ConnectionManager;
import ua.com.rtim.university.util.ScriptRunner;

public class DaoTest {

	protected ConnectionManager connectionManager = new ConnectionManager();
	protected ScriptRunner scriptRunner = new ScriptRunner();
	protected GroupDao groupDao = new GroupDao(connectionManager);
	protected StudentDao studentDao = new StudentDao(connectionManager, groupDao);
	protected CourseDao courseDao = new CourseDao(connectionManager, studentDao);

	protected IDataSet getDataSet(String datasetName) throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream(datasetName));
	}
}