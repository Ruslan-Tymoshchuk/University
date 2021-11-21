package ua.com.rtim.university.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGeneratorDao {

	public static final String INSERT_GROUPS_QUERY = "INSERT INTO groups (group_name) VALUES (?)";
	public static final String INSERT_COURSES_QUERY = "INSERT INTO courses (course_name, course_description) VALUES (?,?)";
	public static final String INSERT_STUDENTS_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?)";
	public static final String INSERT_STUDENTS_COURSES_QUERY = "INSERT INTO students_courses (student_id, course_id) VALUES (?,?)";
	private final List<Student> students;
	private final List<Course> courses;
	private final List<Group> groups;
	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataGeneratorDao.class);

	public DataGeneratorDao(List<Student> students, List<Course> courses, List<Group> groups) {
		this.students = students;
		this.courses = courses;
		this.groups = groups;
	}

	public void insertRandomData() {
		insertGroups();
		insertCourses();
		insertStudents();
		insertStudentsCourses();
		log.info("Data generated and successfully added to the database!");
	}

	private void insertGroups() {
		groups.forEach(group -> {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(INSERT_GROUPS_QUERY,
							Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, group.getGroupName());
				statement.execute();
				try (ResultSet result = statement.getGeneratedKeys()) {
					result.next();
					group.setGroupId(result.getInt("group_id"));
				}
			} catch (SQLException e) {
				log.error("Cannot insert groups", e);
			}
		});
	}

	private void insertCourses() {
		courses.forEach(course -> {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(INSERT_COURSES_QUERY,
							Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, course.getCourseName());
				statement.setString(2, course.getCourseDescription());
				statement.execute();
				try (ResultSet result = statement.getGeneratedKeys()) {
					result.next();
					course.setCourseId(result.getInt("course_id"));
				}
			} catch (SQLException e) {
				log.error("Cannot insert courses", e);
			}
		});
	}

	private void insertStudents() {
		students.forEach(student -> {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(INSERT_STUDENTS_QUERY,
							Statement.RETURN_GENERATED_KEYS)) {
				statement.setInt(1, student.getGroup().getGroupId());
				statement.setString(2, student.getFirstName());
				statement.setString(3, student.getLastName());
				statement.execute();
				try (ResultSet result = statement.getGeneratedKeys()) {
					result.next();
					student.setStudentId(result.getInt("student_id"));
				}
			} catch (SQLException e) {
				log.error("Cannot insert students", e);
			}
		});
	}

	private void insertStudentsCourses() {
		students.forEach(student -> student.getCourses().forEach(course -> {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(INSERT_STUDENTS_COURSES_QUERY)) {
				statement.setInt(1, student.getStudentId());
				statement.setInt(2, course.getCourseId());
				statement.execute();
			} catch (SQLException e) {
				log.error("Cannot insert students and courses", e);
			}
		}));
	}
}
