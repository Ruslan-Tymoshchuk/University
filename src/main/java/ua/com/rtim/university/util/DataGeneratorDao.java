package ua.com.rtim.university.util;

import java.util.List;

import org.apache.log4j.Logger;

import ua.com.rtim.university.dao.CrudRepository;
import ua.com.rtim.university.dao.DaoException;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGeneratorDao {

	private static Logger log = Logger.getLogger(DataGeneratorDao.class);
	public static final String ERROR_MESSAGE = "Failed to create test data";

	public DataGeneratorDao(List<Group> groups, CrudRepository<Group> groupDaoRepository, List<Course> courses,
			CrudRepository<Course> courseDaoRepository, List<Student> students,
			CrudRepository<Student> studentDaoRepository) {
		insertGroups(groups, groupDaoRepository);
		insertCourses(courses, courseDaoRepository);
		insertStudents(students, studentDaoRepository);
		log.info("Data generated and successfully added to the database!");
	}

	private void insertGroups(List<Group> groups, CrudRepository<Group> groupDao) {
		groups.forEach(group -> {
			try {
				groupDao.create(group);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	private void insertCourses(List<Course> courses, CrudRepository<Course> courseDao) {
		courses.forEach(course -> {
			try {
				courseDao.create(course);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	private void insertStudents(List<Student> students, CrudRepository<Student> studentDao) {
		students.forEach(student -> {
			try {
				studentDao.create(student);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}
}