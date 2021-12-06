package ua.com.rtim.university.ui;

import static java.lang.System.lineSeparator;

import java.util.Scanner;

import org.apache.log4j.Logger;

import ua.com.rtim.university.dao.CourseDao;
import ua.com.rtim.university.dao.CrudRepository;
import ua.com.rtim.university.dao.GroupDao;
import ua.com.rtim.university.dao.StudentDao;
import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ConnectionManager;
import ua.com.rtim.university.util.DataGenerator;
import ua.com.rtim.university.util.ScriptRunner;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	public static final String DIALOGUE_MENU = "Application menu:";
	public static final String UNSWER_MENU_FIND_ALL_GROUPS_BY_STUDENT_COUNT = "a. Find all groups with less or equals student count";
	public static final String UNSWER_MENU_FIND_ALL_STUDENTS_BY_COURSE_NAME = "b. Find all students related to course with given name";
	public static final String UNSWER_MENU_ADD_NEW_STUDENT = "c. Add new student";
	public static final String UNSWER_MENU_DELETE_STUDENT_BY_ID = "d. Delete student by STUDENT_ID";
	public static final String UNSWER_MENU_ADD_A_STUDENT_TO_THE_COURSE = "e. Add a student to the course (from a list)";
	public static final String UNSWER_MENU_REMOVE_FROM_COURSE = "f. Remove the student from one of his or her courses";
	public static final String UNSWER_MENU_YOUR_CHOICE = "Your choice: ";

	public static void main(String[] args) {
		log.info(DIALOGUE_MENU + lineSeparator() + UNSWER_MENU_FIND_ALL_GROUPS_BY_STUDENT_COUNT + lineSeparator()
				+ UNSWER_MENU_FIND_ALL_STUDENTS_BY_COURSE_NAME + lineSeparator() + UNSWER_MENU_ADD_NEW_STUDENT
				+ lineSeparator() + UNSWER_MENU_DELETE_STUDENT_BY_ID + lineSeparator()
				+ UNSWER_MENU_ADD_A_STUDENT_TO_THE_COURSE + lineSeparator() + UNSWER_MENU_REMOVE_FROM_COURSE);
		ConnectionManager connectionManager = new ConnectionManager();
		ScriptRunner scriptRunner = new ScriptRunner();
		scriptRunner.generateDatabaseData(connectionManager, "schema.sql");
		CrudRepository<Group> groupDao = new GroupDao(connectionManager);
		CrudRepository<Course> courseDao = new CourseDao(connectionManager,
				new StudentDao(connectionManager, new GroupDao(connectionManager)));
		CrudRepository<Student> studentDao = new StudentDao(connectionManager, new GroupDao(connectionManager));
		DataGenerator dataGenerator = new DataGenerator();
		dataGenerator.generateRandomData(groupDao, courseDao, studentDao);
		UserMenu menu = new Menu(groupDao, courseDao, studentDao);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			switch (scanner.nextLine()) {
			case "a":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_FIND_ALL_GROUPS_BY_STUDENT_COUNT);
				menu.findAllGroupsByStudentsAmount(scanner);
				break;
			case "b":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_FIND_ALL_STUDENTS_BY_COURSE_NAME);
				menu.findAllStudentsByCourseName(scanner);
				break;
			case "c":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_ADD_NEW_STUDENT);
				menu.addNewStudent(scanner);
				break;
			case "d":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_DELETE_STUDENT_BY_ID);
				menu.deleteStudentByStudentId(scanner);
				break;
			case "e":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_ADD_A_STUDENT_TO_THE_COURSE);
				menu.studentToCourseFromList(scanner);
				break;
			case "f":
				log.info(UNSWER_MENU_YOUR_CHOICE + UNSWER_MENU_REMOVE_FROM_COURSE);
				menu.removeStudentFromCourse(scanner);
				break;
			default:
				break;
			}
		}
		scanner.close();
	}
}