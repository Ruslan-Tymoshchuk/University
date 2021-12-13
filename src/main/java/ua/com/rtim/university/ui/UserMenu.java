package ua.com.rtim.university.ui;

import java.util.Scanner;

public interface UserMenu {

	void findAllGroupsByStudentsAmount(Scanner scanner);

	void findAllStudentsByCourseName(Scanner scanner);

	void addNewStudent(Scanner scanner);

	void deleteStudentByStudentId(Scanner scanner);

	void studentToCourseFromList(Scanner scanner);

	void removeStudentFromCourse(Scanner scanner);

}