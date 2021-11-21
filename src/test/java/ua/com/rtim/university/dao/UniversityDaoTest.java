package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ScriptRunner;

class UniversityDaoTest {

	private UniversityDao universityDao = new UniversityDao();
	private GroupDao groupDao = new GroupDao();
	private StudentDao studentDao = new StudentDao();
	private CourseDao courseDao = new CourseDao();
	private ScriptRunner scriptRunner = new ScriptRunner();

	@BeforeEach
	void generateTestData_beforeEachTest_thenTheTestResult() {
		scriptRunner.generateDatabaseData("tables.sql");
		scriptRunner.generateDatabaseData("groupdata.sql");
		scriptRunner.generateDatabaseData("studentdata.sql");
		scriptRunner.generateDatabaseData("coursedata.sql");
	}

	@Test
	void shouldBeGet_GroupsByStudentAmount_fromTheDataBase() {
		List<Group> expected = new ArrayList<>();
		expected.add(groupDao.getById(1));
		expected.add(groupDao.getById(2));
		expected.add(groupDao.getById(3));
		assertEquals(expected, universityDao.getGroupsByStudentAmount(1));
	}

	@Test
	void shouldBeGet_AllStudentsByCourseName_fromTheDataBase() {
		Student student = studentDao.getById(1);
		Course course = new Course();
		course.setCourseId(1);
		universityDao.addToCourse(student, course);
		assertEquals(Arrays.asList(studentDao.getById(1)), universityDao.findAllStudentsByCourseName("PHYSICS"));
	}

	@Test
	void shouldBeRemove_studentFromCourse_inTheDataBase() {
		Student student = studentDao.getById(1);
		Course course = courseDao.getById(1);
		universityDao.addToCourse(student, course);
		universityDao.removeFromCourse(student, course);
		student = studentDao.getById(1);
		assertTrue(student.getCourses().isEmpty());
	}
}
