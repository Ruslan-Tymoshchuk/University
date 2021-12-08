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
import ua.com.rtim.university.domain.Student;

class StudentDaoTest extends DaoTest {

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Student> students = studentDao.findAll();
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("STUDENTS");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), students.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Student student = new Student();
			student.setId(4);
			student.setFirstName("Marquis");
			student.setLastName("Martin");
			Group group = new Group();
			group.setId(3);
			student.setGroup(group);
			studentDao.create(student);
			ITable expectedTable = getDataSet("expected-create.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
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
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Student student = studentDao.getById(3).get();
			student.setFirstName("TestFirstName");
			student.setLastName("TestLastName");
			student.setGroup(groupDao.getById(1).get());
			studentDao.update(student);
			ITable expectedTable = getDataSet("expected-update.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
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
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			studentDao.delete(studentDao.getById(3).get());
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeAdd_studentToCourse_inTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			studentDao.addToCourse(3, 1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS_COURSES");
			connection.close();
			ITable expectedTable = getDataSet("expected-update.xml").getTable("STUDENTS_COURSES");
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeGet_AllStudentsByCourse_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("expected-update.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Student> studentsByCourseName = studentDao.findAllStudentsByCourse(1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			connection.close();
			ITable expectedTable = getDataSet("expected-update.xml").getTable("STUDENTS");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), studentsByCourseName.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenCreateGroup_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> studentDao.create(null));
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
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> studentDao.update(null));
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
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> studentDao.delete(null));
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