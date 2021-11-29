package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

class UniversityDaoTest extends DaoTest {

	@Test
	void shouldBeGet_GroupsByStudentAmount_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Group> groupsByStudentAmount = universityDao.getGroupsByStudentAmount(1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), groupsByStudentAmount.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeGet_AllStudentsByCourseName_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			List<Student> studentsByCourseName = universityDao.findAllStudentsByCourseName("PHYSICS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("STUDENTS");
			Assertion.assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), studentsByCourseName.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeAdd_studentToCourse_inTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Student student = studentDao.getById(1);
			Course course = courseDao.getById(2);
			universityDao.addToCourse(student, course);
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
	void shouldBeRemove_studentFromCourse_inTheDataBase() throws Exception {
		IDatabaseConnection connection = new DatabaseConnection(connectionManager.getConnection(), "public");
		try {
			scriptRunner.generateDatabaseData("tables.sql");
			IDataSet dataSet = getDataSet("actualdata.xml");
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			Student student = studentDao.getById(1);
			Course course = courseDao.getById(1);
			universityDao.removeFromCourse(student, course);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS_COURSES");
			connection.close();
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("STUDENTS_COURSES");
			Assertion.assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}
}