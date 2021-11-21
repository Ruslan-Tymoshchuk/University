package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class StudentDao extends DaoFactory<Student> {

	public static final String GET_ALL_STUDENTS_QUERY = "SELECT student_id FROM students";
	public static final String ADD_NEW_STUDENT_QUERY = "INSERT INTO students (first_name, last_name) VALUES (?,?)";
	public static final String GET_STUDENT_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?";
	public static final String UPDATE_STUDENT_QUERY = "UPDATE students SET first_name = ?, last_name = ? WHERE student_id = ?";
	public static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
	public static final String FIND_COURSES_BY_STUDENT_QUERY = "SELECT c.* FROM students_courses sc "
			+ "LEFT JOIN courses c on c.course_id = sc.course_id WHERE student_id = ?";

	@Override
	public List<Student> findAll() {
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(GET_ALL_STUDENTS_QUERY)) {
				while (result.next()) {
					students.add(getById(result.getRow()));
				}
			}
			log.info("The search was successful");
		} catch (SQLException e) {
			log.error("I can't find all students", e);
		}
		return students;
	}

	@Override
	public void create(Student student) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_NEW_STUDENT_QUERY)) {
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.execute();
			log.info("Ok, student has been added");
		} catch (SQLException e) {
			log.error("Failed to create a student", e);
		}
	}

	@Override
	public Student getById(int studentId) {
		Student student = new Student();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, studentId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					student.setStudentId(result.getInt("student_id"));
					student.setFirstName(result.getString("first_name"));
					student.setLastName(result.getString("last_name"));
					Set<Course> coursesByStudent = findCoursesByStudent(student);
					coursesByStudent.forEach(course -> student.setCourse(course));
					Group group = new GroupDao().getById(result.getInt("group_id"));
					student.setGroup(group);
				}
			}
		} catch (SQLException e) {
			log.error("Student whith id-" + studentId + " not found", e);
		}
		return student;
	}

	@Override
	public void update(Student student) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_QUERY)) {
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.setInt(3, student.getStudentId());
			statement.execute();
			log.info("Ok, student has been update");
		} catch (SQLException e) {
			log.error("Update error", e);
		}
	}

	@Override
	public void delete(Student student) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, student.getStudentId());
			statement.execute();
			log.info("Ok, student has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
		}
	}

	private Set<Course> findCoursesByStudent(Student student) {
		Set<Course> courses = new HashSet<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_QUERY)) {
			statement.setInt(1, student.getStudentId());
			statement.execute();
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					Course course = new Course();
					course.setCourseId(result.getInt("course_id"));
					course.setCourseName(result.getString("course_name"));
					course.setDescription(result.getString("course_description"));
					courses.add(course);
				}
			}
		} catch (SQLException e) {
			log.error("Error in finding courses", e);
		}
		return courses;
	}
}
