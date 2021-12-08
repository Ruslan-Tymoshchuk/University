package ua.com.rtim.university.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import ua.com.rtim.university.dao.CourseDao;
import ua.com.rtim.university.dao.DaoException;
import ua.com.rtim.university.dao.GroupDao;
import ua.com.rtim.university.dao.StudentDao;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class Menu implements UserMenu {

	private static Logger log = Logger.getLogger(Menu.class);

	public static final String UNSWER_MENU_ENTER_THE_NUMBER = "Enter the number!";
	public static final String SPACE_DELIMITER = " ";
	public static final String UNSWER_MENU_TO_MAIN_MENU = "Going to the main menu!";
	public static final String UNSWER_MENU_COURSE_NAME = "Enter the course ID: ";
	public static final String UNSWER_MENU_NO_STUDENTS_WHITH_THAT_NAME = "There were no students with that name!";
	public static final String UNSWER_MENU_FIRST_NAME = "Enter first name";
	public static final String UNSWER_MENU_LAST_NAME = "Enter last name";
	public static final String UNSWER_MENU_GROUP_FOR_STUDENT = "Select the group for a student. Enter Group ID";
	public static final String UNSWER_MENU_STUDENT_BEEN_ADDED = "Ok, student has been added";
	public static final String UNSWER_MENU_INCORRECT_GROUP_CELECTION = "Incorrect group selection!";
	public static final String UNSWER_MENU_ENTER_STUDENT_ID = "Enter student ID";
	public static final String UNSWER_MENU_ENTER_COURSE_ID = "Enter course ID";
	public static final String UNSWER_MENU_INCORRECT_COURSE_CELECTION = "Incorrect course selection!";
	public static final String STUDENT_FORMAT = "Student: ID_%d %s %s";
	public static final String GROUP_FORMAT = "Group: ID_%d %s";
	public static final String COURSE_FORMAT = "Course: ID_%d %s %s";

	private final GroupDao groupDao;
	private final CourseDao courseDao;
	private final StudentDao studentDao;

	public Menu(GroupDao groupDao, CourseDao courseDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.courseDao = courseDao;
		this.studentDao = studentDao;
	}

	@Override
	public void findAllGroupsByStudentsAmount(Scanner scanner) {
		try {
			System.out.println(UNSWER_MENU_ENTER_THE_NUMBER);
			List<Group> groups = groupDao.findGroupsByStudentsAmount(scanner.nextInt());
			groups.forEach(group -> System.out.println(group.getName()));
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void findAllStudentsByCourseName(Scanner scanner) {
		try {
			List<Course> courses = courseDao.findAll();
			courses.forEach(course -> System.out
					.println(String.format(COURSE_FORMAT, course.getId(), course.getName(), course.getDescription())));
			System.out.println(UNSWER_MENU_COURSE_NAME);
			Course course = courseDao.getById(scanner.nextInt()).orElseThrow(InputMismatchException::new);
			List<Student> studentsByCourse = course.getStudents();
			if (!studentsByCourse.isEmpty()) {
				studentsByCourse.forEach(student -> System.out.println(
						String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName())));
			} else {
				System.out
						.println(UNSWER_MENU_NO_STUDENTS_WHITH_THAT_NAME + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void addNewStudent(Scanner scanner) {
		Student student = new Student();
		System.out.println(UNSWER_MENU_FIRST_NAME);
		student.setFirstName(scanner.nextLine());
		System.out.println(UNSWER_MENU_LAST_NAME);
		student.setLastName(scanner.nextLine());
		System.out.println(UNSWER_MENU_GROUP_FOR_STUDENT);
		try {
			List<Group> groups = groupDao.findAll();
			groups.forEach(group -> System.out.println(String.format(GROUP_FORMAT, group.getId(), group.getName())));
			int id = scanner.nextInt();
			Group group = groupDao.getById(id).orElseThrow(InputMismatchException::new);
			student.setGroup(group);
			studentDao.create(student);
			System.out.println(UNSWER_MENU_STUDENT_BEEN_ADDED);
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_INCORRECT_GROUP_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void deleteStudentByStudentId(Scanner scanner) {
		try {
			System.out.println(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt()).orElseThrow(InputMismatchException::new);
			if (student.getId() > 0) {
				System.out.println(
						String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName()));
				studentDao.delete(student);
			} else {
				System.out.println(UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void studentToCourseFromList(Scanner scanner) {
		try {
			List<Course> courses = courseDao.findAll();
			courses.forEach(course -> System.out
					.println(String.format(COURSE_FORMAT, course.getId(), course.getName(), course.getDescription())));
			System.out.println(UNSWER_MENU_ENTER_STUDENT_ID);
			int studentId = scanner.nextInt();
			Student student = studentDao.getById(studentId).orElseThrow(InputMismatchException::new);
			System.out.println(
					String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName()));
			System.out.println(UNSWER_MENU_ENTER_COURSE_ID);
			int courseId = scanner.nextInt();
			Course course = courseDao.getById(courseId).orElseThrow(InputMismatchException::new);
			if (!course.getStudents().contains(student)) {
				studentDao.addToCourse(studentId, courseId);
				System.out.println("Ok, the student has been added to the course");
			} else {
				System.out.println(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void removeStudentFromCourse(Scanner scanner) {
		try {
			System.out.println(UNSWER_MENU_ENTER_STUDENT_ID);
			int studentId = scanner.nextInt();
			Set<Course> coursesByStudent = courseDao.findCoursesByStudent(studentId);
			coursesByStudent.forEach(course -> System.out
					.println(String.format(COURSE_FORMAT, course.getId(), course.getName(), course.getDescription())));
			System.out.println(UNSWER_MENU_ENTER_COURSE_ID);
			int courseId = scanner.nextInt();
			Course course = courseDao.getById(courseId).orElseThrow(InputMismatchException::new);
			if (coursesByStudent.contains(course)) {
				courseDao.removeFromCourse(studentId, course.getId());
				System.out.println("The student has been removed from the course");
			} else {
				System.out.println(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}
}