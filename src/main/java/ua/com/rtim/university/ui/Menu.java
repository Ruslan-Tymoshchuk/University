package ua.com.rtim.university.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ua.com.rtim.university.dao.CourseDao;
import ua.com.rtim.university.dao.StudentDao;
import ua.com.rtim.university.dao.UniversityDao;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class Menu implements UserMenu {

	public static final String UNSWER_MENU_ENTER_THE_NUMBER = "Enter the number!";
	public static final String SPACE_DELIMITER = " ";
	public static final String UNSWER_MENU_TO_MAIN_MENU = "Going to the main menu!";
	public static final String COURSE_FORMAT = "Course: ID_%d %s";
	public static final String UNSWER_MENU_COURSE_NAME = "Enter the course name: ";
	public static final String UNSWER_MENU_NO_STUDENTS_WHITH_THAT_NAME = "There were no students with that name!";
	public static final String UNSWER_MENU_FIRST_NAME = "Enter first name";
	public static final String UNSWER_MENU_LAST_NAME = "Enter last name";
	public static final String UNSWER_MENU_ENTER_STUDENT_ID = "Enter student ID";
	public static final String UNSWER_MENU_ENTER_COURSE_ID = "Enter course ID";
	public static final String UNSWER_MENU_INCORRECT_COURSE_CELECTION = "Incorrect course selection!";
	public static final String STUDENT_FORMAT = "Student: ID_%d %s %s";
	private UniversityManager userOption = new UniversityDao();
	private CrudRepository<Course> repository = new CourseDao();
	private CrudRepository<Student> studentDao = new StudentDao();
	private CrudRepository<Course> courseDao = new CourseDao();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Menu.class);

	@Override
	public void findAllGroupsByStudentsAmount(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_THE_NUMBER);
			List<Group> groups = userOption.getGroupsByStudentAmount(scanner.nextInt());
			groups.forEach(group -> log.info(group.getGroupName()));
		} catch (InputMismatchException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
		}
	}

	@Override
	public void findAllStudentsByCourseName(Scanner scanner) {
		List<Course> courses = repository.findAll();
		courses.forEach(course -> log.info(String.format(COURSE_FORMAT, course.getCourseId(), course.getCourseName())));
		log.info(UNSWER_MENU_COURSE_NAME);
		List<Student> studentsByCourse = userOption.findAllStudentsByCourseName(scanner.nextLine());
		if (!studentsByCourse.isEmpty()) {
			studentsByCourse.forEach(student -> log.info(String.format(STUDENT_FORMAT, student.getStudentId(),
					student.getFirstName(), student.getLastName())));
		} else {
			log.info(UNSWER_MENU_NO_STUDENTS_WHITH_THAT_NAME + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
		}
	}

	@Override
	public void addNewStudent(Scanner scanner) {
		Student student = new Student();
		log.info(UNSWER_MENU_FIRST_NAME);
		student.setFirstName(scanner.nextLine());
		log.info(UNSWER_MENU_LAST_NAME);
		student.setLastName(scanner.nextLine());
		studentDao.create(student);
	}

	@Override
	public void deleteStudentByStudentId(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getStudentId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getStudentId(), student.getFirstName(),
						student.getLastName()));
				studentDao.delete(student);
			} else {
				log.info(UNSWER_MENU_TO_MAIN_MENU);
			}
		} catch (InputMismatchException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
		}
	}

	@Override
	public void studentToCourseFromList(Scanner scanner) {
		List<Course> courses = courseDao.findAll();
		courses.forEach(course -> log.info(String.format(COURSE_FORMAT, course.getCourseId(), course.getCourseName())));
		try {
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getStudentId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getStudentId(), student.getFirstName(),
						student.getLastName()));
				log.info(UNSWER_MENU_ENTER_COURSE_ID);
				Course course = courseDao.getById(scanner.nextInt());
				if (course.getCourseId() > 0 && !student.getCourses().contains(course)) {
					userOption.addToCourse(student, course);
				} else {
					log.info(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
				}
			}
		} catch (InputMismatchException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
		}
	}

	@Override
	public void removeStudentFromCourse(Scanner scanner) {
		try {
			log.info(UNSWER_MENU_ENTER_STUDENT_ID);
			Student student = studentDao.getById(scanner.nextInt());
			if (student.getStudentId() > 0) {
				log.info(String.format(STUDENT_FORMAT, student.getStudentId(), student.getFirstName(),
						student.getLastName()));
				student.getCourses().forEach(
						course -> log.info(String.format(COURSE_FORMAT, course.getCourseId(), course.getCourseName())));
				log.info(UNSWER_MENU_ENTER_COURSE_ID);
				Course course = courseDao.getById(scanner.nextInt());
				if (student.getCourses().contains(course)) {
					userOption.removeFromCourse(student, course);
				} else {
					log.info(UNSWER_MENU_INCORRECT_COURSE_CELECTION + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
				}
			}
		} catch (InputMismatchException e) {
			log.error(UNSWER_MENU_ENTER_THE_NUMBER + SPACE_DELIMITER + UNSWER_MENU_TO_MAIN_MENU);
		}
	}
}
