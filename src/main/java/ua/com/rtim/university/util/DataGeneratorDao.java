package ua.com.rtim.university.util;

import static java.util.concurrent.ThreadLocalRandom.current;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import ua.com.rtim.university.dao.CourseDao;
import ua.com.rtim.university.dao.DaoException;
import ua.com.rtim.university.dao.GroupDao;
import ua.com.rtim.university.dao.StudentDao;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGeneratorDao {

	private static Logger log = Logger.getLogger(DataGeneratorDao.class);

	public static final String ERROR_MESSAGE = "Failed to create test data";

	private final GroupDao groupDao;
	private final CourseDao courseDao;
	private final StudentDao studentDao;

	public DataGeneratorDao(GroupDao groupDao, CourseDao courseDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.courseDao = courseDao;
		this.studentDao = studentDao;
	}

	public void insertGroups(List<Group> groups, List<Student> students, int minGroupSize, int maxGroupSize) {
		Map<Group, List<Student>> groupsStudents = new HashMap<>();
		students.forEach(student -> {
			Group group = groups.get(current().nextInt(groups.size()));
			groupsStudents.computeIfAbsent(group, s -> new ArrayList<>()).add(student);
		});
		groupsStudents.forEach((group, studentsList) -> {
			try {
				groupDao.create(group);
				if (studentsList.size() >= minGroupSize && studentsList.size() <= maxGroupSize) {
					studentsList.forEach(student -> student.setGroup(group));
				}
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	public void insertCourses(List<Course> courses) {
		courses.forEach(course -> {
			try {
				courseDao.create(course);
			} catch (DaoException e) {
				log.error(ERROR_MESSAGE, e);
			}
		});
	}

	public void insertStudents(List<Student> allStudents, List<Course> courses) {
		allStudents.forEach(student -> {
			if (student.getGroup() != null) {
				try {
					Set<Course> studentCourses = new HashSet<>();
					IntStream.range(0, current().nextInt(1, 4)).forEach(s -> {
						Course course = courses.get(current().nextInt(courses.size()));
						studentCourses.add(course);
					});
					student.setCourses(studentCourses);
					studentDao.create(student);
				} catch (DaoException e) {
					log.error(ERROR_MESSAGE, e);
				}
			}
		});
	}
}