package ua.com.rtim.university.ui;

import static java.lang.System.lineSeparator;

import java.util.Scanner;

import ua.com.rtim.university.util.DataGenerator;
import ua.com.rtim.university.util.DataGeneratorDao;
import ua.com.rtim.university.util.ScriptRunner;

public class Main {

	public static final String DIALOGUE_MENU = "Application menu:";
	public static final String UNSWER_MENU_FIND_ALL_GROUPS_BY_STUDENT_COUNT = "a. Find all groups with less or equals student count";
	public static final String UNSWER_MENU_FIND_ALL_STUDENTS_BY_COURSE_NAME = "b. Find all students related to course with given name";
	public static final String UNSWER_MENU_ADD_NEW_STUDENT = "c. Add new student";
	public static final String UNSWER_MENU_DELETE_STUDENT_BY_ID = "d. Delete student by STUDENT_ID";
	public static final String UNSWER_MENU_ADD_A_STUDENT_TO_THE_COURSE = "e. Add a student to the course (from a list)";
	public static final String UNSWER_MENU_REMOVE_FROM_COURSE = "f. Remove the student from one of his or her courses";
	public static final String UNSWER_MENU_YOUR_CHOICE = "Your choice: ";
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		log.info(DIALOGUE_MENU + lineSeparator() + UNSWER_MENU_FIND_ALL_GROUPS_BY_STUDENT_COUNT + lineSeparator()
				+ UNSWER_MENU_FIND_ALL_STUDENTS_BY_COURSE_NAME + lineSeparator() + UNSWER_MENU_ADD_NEW_STUDENT
				+ lineSeparator() + UNSWER_MENU_DELETE_STUDENT_BY_ID + lineSeparator()
				+ UNSWER_MENU_ADD_A_STUDENT_TO_THE_COURSE + lineSeparator() + UNSWER_MENU_REMOVE_FROM_COURSE);
		ScriptRunner scriptRunner = new ScriptRunner();
		scriptRunner.generateDatabaseData("tables.sql");
		DataGeneratorDao dataGeneratordao = new DataGenerator().generateRandomData();
		dataGeneratordao.insertRandomData();
		Scanner scanner = new Scanner(System.in);
		UserMenu menu = new Menu();
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
