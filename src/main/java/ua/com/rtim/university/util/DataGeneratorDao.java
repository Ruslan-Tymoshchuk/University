package ua.com.rtim.university.util;

import java.util.List;

import ua.com.rtim.university.dao.CourseDao;
import ua.com.rtim.university.dao.CrudRepository;
import ua.com.rtim.university.dao.DaoException;
import ua.com.rtim.university.dao.GroupDao;
import ua.com.rtim.university.dao.StudentDao;
import ua.com.rtim.university.dao.UniversityDao;
import ua.com.rtim.university.dao.UniversityManager;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGeneratorDao {

	public static final String ERROR_MESSAGE = "Failed to create test data";
	private final List<Student> students;
	private final List<Course> courses;
	private final List<Group> groups;
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataGeneratorDao.class);

	public DataGeneratorDao(List<Student> students, List<Course> courses, List<Group> groups) {
		this.students = students;
		this.courses = courses;
		this.groups = groups;
	}

	public void insertRandomData() {
		CrudRepository<Group> groupDao = new GroupDao();
		CrudRepository<Course> courseDao = new CourseDao();
		CrudRepository<Student> studentDao = new StudentDao();
		UniversityManager universityManager = new UniversityDao();
		insertGroups(groupDao);
		insertCourses(courseDao);
		insertStudents(studentDao);
		insertStudentsCourses(universityManager);
		log.info("Data generated and successfully added to the database!");
	}

	private void insertGroups(CrudRepository<Group> groupDao) {
		groups.forEach(group -> {
			try {
				groupDao.create(group);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	private void insertCourses(CrudRepository<Course> courseDao) {
		courses.forEach(course -> {
			try {
				courseDao.create(course);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	private void insertStudents(CrudRepository<Student> studentDao) {
		students.forEach(student -> {
			try {
				studentDao.create(student);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	private void insertStudentsCourses(UniversityManager universityManager) {
		students.forEach(student -> student.getCourses().forEach(course -> {
			try {
				universityManager.addToCourse(student, course);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		}));
	}
}