package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ScriptRunner;

class StudentDaoTest {

	private StudentDao studentDao = new StudentDao();
	private GroupDao groupDao = new GroupDao();
	private ScriptRunner scriptRunner = new ScriptRunner();

	@BeforeEach
	void generateTestData_beforeEachTest_thenTheTestResult() {
		scriptRunner.generateDatabaseData("tables.sql");
		scriptRunner.generateDatabaseData("groupdata.sql");
		scriptRunner.generateDatabaseData("studentdata.sql");
	}

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() {
		List<Student> expected = new ArrayList<>();
		Student student1 = new Student();
		student1.setStudentId(1);
		student1.setFirstName("Emily");
		student1.setLastName("Moore");
		student1.setCourse(new Course());
		student1.setGroup(groupDao.getById(1));
		expected.add(student1);
		Student student2 = new Student();
		student2.setStudentId(2);
		student2.setFirstName("Xavier");
		student2.setLastName("Davis");
		student2.setCourse(new Course());
		student2.setGroup(groupDao.getById(2));
		expected.add(student2);
		Student student3 = new Student();
		student3.setStudentId(3);
		student3.setFirstName("Dustin");
		student3.setLastName("Johnson");
		student3.setCourse(new Course());
		student3.setGroup(groupDao.getById(3));
		expected.add(student3);
		assertEquals(expected, studentDao.findAll());
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() {
		Student expected = new Student();
		expected.setStudentId(4);
		expected.setFirstName("Katie");
		expected.setLastName("Wilson");
		expected.setGroup(new Group());
		studentDao.create(expected);
		Student actual = studentDao.getById(4);
		assertEquals(expected, actual);
	}

	@Test
	void update_shouldBeUpdateEntity_inTheDataBase() {
		Student expected = studentDao.getById(1);
		expected.setFirstName("TestFirstName");
		expected.setLastName("TestLastName");
		studentDao.update(expected);
		assertEquals(expected, studentDao.getById(1));
	}

	@Test
	void delete_shouldBeRemoveEntity_fromTheDataBase() {
		studentDao.delete(studentDao.getById(1));
		Student expected = new Student();
		assertEquals(expected, studentDao.getById(1));
	}

	@Test
	void givenNull_whenCreateGroup_thenException() {
		assertThrows(NullPointerException.class, () -> studentDao.create(null));
	}

	@Test
	void givenNull_whenUpdateGroup_thenException() {
		assertThrows(NullPointerException.class, () -> studentDao.update(null));
	}

	@Test
	void givenNull_whenDeleteGroup_thenException() {
		assertThrows(NullPointerException.class, () -> studentDao.delete(null));
	}

}
