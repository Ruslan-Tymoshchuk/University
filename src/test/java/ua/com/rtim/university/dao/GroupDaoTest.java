package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Group;

class GroupDaoTest extends DaoTest {

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Group> groups = groupDao.findAll();
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), groups.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Group group = new Group();
			group.setId(4);
			group.setName("HK-23");
			groupDao.create(group);
			ITable expectedTable = getDataSet("expected-create.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void update_shouldBeUpdateEntity_inTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Group group = groupDao.getById(3);
			group.setName("HH-55");
			groupDao.update(group);
			ITable expectedTable = getDataSet("expected-update.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void delete_shouldBeRemoveEntity_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			groupDao.delete(groupDao.getById(3));
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenCreateGroup_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> groupDao.create(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenUpdateGroup_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> groupDao.update(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenDeleteGroup_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> groupDao.delete(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}
}