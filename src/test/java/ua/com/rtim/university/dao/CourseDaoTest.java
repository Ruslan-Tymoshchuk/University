package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.util.ScriptRunner;

class CourseDaoTest {

	private CourseDao courseDao = new CourseDao();
	private ScriptRunner scriptRunner = new ScriptRunner();

	@BeforeEach
	void generateTestData_beforeEachTest_thenTheTestResult() {
		scriptRunner.generateDatabaseData("tables.sql");
		scriptRunner.generateDatabaseData("coursedata.sql");
	}

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() {
		List<Course> expected = new ArrayList<>();
		Course course1 = new Course();
		course1.setCourseId(1);
		course1.setCourseName("PHYSICS");
		course1.setDescription("The best science");
		expected.add(course1);
		Course course2 = new Course();
		course2.setCourseId(2);
		course2.setCourseName("HYSTORY");
		course2.setDescription("The best science");
		expected.add(course2);
		Course course3 = new Course();
		course3.setCourseId(3);
		course3.setCourseName("ENGLISH");
		course3.setDescription("The best science");
		expected.add(course3);
		assertEquals(expected, courseDao.findAll());
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() {
		Course expected = new Course();
		expected.setCourseId(4);
		expected.setCourseName("TestCourse");
		expected.setDescription("The best science");
		courseDao.create(expected);
		Course actual = courseDao.getById(4);
		assertEquals(expected, actual);
	}

	@Test
	void update_shouldBeUpdateEntity_inTheDataBase() {
		Course expected = courseDao.getById(1);
		expected.setCourseName("TestName");
		expected.setDescription("TestDescription");
		courseDao.update(expected);
		assertEquals(expected, courseDao.getById(1));
	}

	@Test
	void delete_shouldBeRemoveEntity_fromTheDataBase() {
		courseDao.delete(courseDao.getById(1));
		Course expected = new Course();
		assertEquals(expected, courseDao.getById(1));
	}

	@Test
	void givenNull_whenCreateCourse_thenException() {
		assertThrows(NullPointerException.class, () -> courseDao.create(null));
	}

	@Test
	void givenNull_whenUpdateCourse_thenException() {
		assertThrows(NullPointerException.class, () -> courseDao.update(null));
	}

	@Test
	void givenNull_whenDeleteCourse_thenException() {
		assertThrows(NullPointerException.class, () -> courseDao.delete(null));
	}
}
