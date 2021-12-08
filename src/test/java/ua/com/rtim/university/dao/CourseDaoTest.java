package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;

class CourseDaoTest extends DaoTest {

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Course> courses = courseDao.findAll();
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("COURSES");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), courses.size());
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
			Course course = new Course();
			course.setId(4);
			course.setName("TestCourse");
			course.setDescription("The best science");
			courseDao.create(course);
			ITable expectedTable = getDataSet("expected-create.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
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
			Course course = courseDao.getById(3).get();
			course.setName("TestName");
			course.setDescription("TestDescription");
			courseDao.update(course);
			ITable expectedTable = getDataSet("expected-update.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
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
			courseDao.delete(courseDao.getById(3).get());
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeRemove_studentFromCourse_inTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			courseDao.removeFromCourse(1, 1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS_COURSES");
			connection.close();
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("STUDENTS_COURSES");
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeGet_AllCoursesByStudent_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Set<Course> studentsByCourseName = courseDao.findCoursesByStudent(1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("COURSES");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), studentsByCourseName.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenCreateCourse_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> courseDao.create(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenUpdateCourse_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> courseDao.update(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenDeleteCourse_thenException() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			assertThrows(NullPointerException.class, () -> courseDao.delete(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("COURSES");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("COURSES");
			connection.close();
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}
}