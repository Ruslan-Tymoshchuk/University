package ua.com.rtim.university.dao;

import java.util.List;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public interface UniversityManager {

	List<Group> getGroupsByStudentAmount(int amount) throws DaoException;

	List<Student> findAllStudentsByCourseName(String courseName) throws DaoException;

	void addToCourse(Student student, Course course) throws DaoException;

	void removeFromCourse(Student student, Course course) throws DaoException;

}