package ua.com.rtim.university.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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

public class Menu implements UserMenu {

	public static final String UNSWER_MENU_ENTER_THE_NUMBER = "Enter the number!";
	public static final String SPACE_DELIMITER = " ";
	public static final String UNSWER_MENU_TO_MAIN_MENU = "Going to the main menu!";
	public static final String UNSWER_MENU_COURSE_NAME = "Enter the course name: ";
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
	public static final String COURSE_FORMAT = "Course: ID_%d %s";
	private UniversityManager userOption = new UniversityDao();
	private CrudRepository<Course> courseDao = new CourseDao();
	private CrudRepository<Student> studentDao = new StudentDao();
	private CrudRepository<Group> groupDao = new GroupDao();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Menu.class);

	@Override
	public void findAllGroupsByStudentsAmount(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_THE_NUMBER);
			List<Group> groups = userOption.getGroupsByStudentAmount(scanner.nextInt());
			groups.forEach(group -> log.info(group.getName()));
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void findAllStudentsByCourseName(Scanner scanner) {
		try {
			List<Course> courses = courseDao.findAll();
			courses.forEach(course -> log.info(String.format(COURSE_FORMAT, course.getId(), course.getName())));
			log.info(UNSWER_MENU_COURSE_NAME);
			List<Student> studentsByCourse = userOption.findAllStudentsByCourseName(scanner.nextLine());
			if (!studentsByCourse.isEmpty()) {
				studentsByCourse.forEach(student -> log.info(
						String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName())));
			} else {
				log.info(UNSWER_MENU_NO_STUDENTS_WHITH_THAT_NAME + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (DaoException e) {
			log.error(UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void addNewStudent(Scanner scanner) {
		Student student = new Student();
		log.info(UNSWER_MENU_FIRST_NAME);
		student.setFirstName(scanner.nextLine());
		log.info(UNSWER_MENU_LAST_NAME);
		student.setLastName(scanner.nextLine());
		log.info(UNSWER_MENU_GROUP_FOR_STUDENT);
		try {
			List<Group> groups = groupDao.findAll();
			groups.forEach(group -> log.info(String.format(GROUP_FORMAT, group.getId(), group.getName())));
			int id = scanner.nextInt();
			Group group = groups.stream().filter(gr -> gr.getId() == id).findFirst()
					.orElseThrow(InputMismatchException::new);
			student.setGroup(group);
			studentDao.create(student);
			log.info(UNSWER_MENU_STUDENT_BEEN_ADDED);
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_INCORRECT_GROUP_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void deleteStudentByStudentId(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName()));
				studentDao.delete(student);
			} else {
				log.info(UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void studentToCourseFromList(Scanner scanner) {
		try {
			List<Course> courses = courseDao.findAll();
			courses.forEach(course -> log.info(String.format(COURSE_FORMAT, course.getId(), course.getName())));
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName()));
				log.info(UNSWER_MENU_ENTER_COURSE_ID);
				Course course = courseDao.getById(scanner.nextInt());
				if (course.getId() > 0 && !student.getCourses().contains(course)) {
					userOption.addToCourse(student, course);
					log.info("Ok, the student has been added to the course");
				} else {
					log.info(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
				}
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}

	@Override
	public void removeStudentFromCourse(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getId(), student.getFirstName(), student.getLastName()));
				student.getCourses()
						.forEach(course -> log.info(String.format(COURSE_FORMAT, course.getId(), course.getName())));
				log.info(UNSWER_MENU_ENTER_COURSE_ID);
				Course course = courseDao.getById(scanner.nextInt());
				if (student.getCourses().contains(course)) {
					userOption.removeFromCourse(student, course);
					log.info("The student has been removed from the course");
				} else {
					log.info(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
				}
			}
		} catch (InputMismatchException | DaoException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU, e);
		}
	}
}