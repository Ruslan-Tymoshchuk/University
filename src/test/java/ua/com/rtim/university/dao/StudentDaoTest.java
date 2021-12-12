package ua.com.rtim.university.dao;

import static org.dbunit.Assertion.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

class StudentDaoTest extends DaoTest {

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			List<Student> students = studentDao.findAll();
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			ITable expectedTable = getDataSet("actualdata.xml").getTable("STUDENTS");
			assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), students.size());
		} finally {
			connection.close();
		}
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			Student student = new Student();
			student.setId(4);
			student.setFirstName("Marquis");
			student.setLastName("Martin");
			Group group = new Group();
			group.setId(3);
			student.setGroup(group);
			Set<Course> courses = new HashSet<>();
			courses.add(courseDao.getById(1).get());
			student.setCourses(courses);
			studentDao.create(student);
			ITable expectedTable = getDataSet("expected-create.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void update_shouldBeUpdateEntity_inTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			Student student = studentDao.getById(3).get();
			student.setFirstName("TestFirstName");
			student.setLastName("TestLastName");
			student.setGroup(groupDao.getById(1).get());
			studentDao.update(student);
			ITable expectedTable = getDataSet("expected-update.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void delete_shouldBeRemoveEntity_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			studentDao.delete(studentDao.getById(3).get());
			ITable expectedTable = getDataSet("expected-delete.xml").getTable("STUDENTS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeAdd_studentToCourse_inTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			studentDao.addToCourse(2, 2);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS_COURSES");
			ITable expectedTable = getDataSet("expected-update.xml").getTable("STUDENTS_COURSES");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void shouldBeGet_AllStudentsByCourse_fromTheDataBase() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			List<Student> studentsByCourseName = studentDao.findAllStudentsByCourse(1);
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("STUDENTS");
			connection.close();
			ITable expectedTable = getDataSet("actualdata.xml").getTable("STUDENTS");
			assertEquals(expectedTable, actualTable);
			assertEquals(expectedTable.getRowCount(), studentsByCourseName.size());
		} finally {
			connection.close();
		}
	}


	@Test
	void givenNull_whenCreateGroup_thenException() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			assertThrows(NullPointerException.class, () -> studentDao.create(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenUpdateGroup_thenException() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			assertThrows(NullPointerException.class, () -> studentDao.update(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}

	@Test
	void givenNull_whenDeleteGroup_thenException() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			assertThrows(NullPointerException.class, () -> studentDao.delete(null));
			ITable expectedTable = getDataSet("actualdata.xml").getTable("GROUPS");
			IDataSet databaseDataSet = connection.createDataSet();
			ITable actualTable = databaseDataSet.getTable("GROUPS");
			assertEquals(expectedTable, actualTable);
		} finally {
			connection.close();
		}
	}
}