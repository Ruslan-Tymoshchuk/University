package ua.com.rtim.university.ui;

import java.util.List;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public interface UniversityManager {

	List<Group> getGroupsByStudentAmount(int amount);

	List<Student> findAllStudentsByCourseName(String courseName);

	void addToCourse(Student student, Course course);

	void removeFromCourse(Student student, Course course);

}
