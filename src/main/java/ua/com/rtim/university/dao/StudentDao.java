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
import ua.com.rtim.university.util.ConnectionManager;

public class StudentDao implements CrudRepository<Student> {

	public static final String GET_ALL_STUDENTS_QUERY = "SELECT * FROM students";
	public static final String ADD_NEW_STUDENT_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?)";
	public static final String GET_STUDENT_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?";
	public static final String UPDATE_STUDENT_QUERY = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
	public static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
	public static final String FIND_COURSES_BY_STUDENT_QUERY = "SELECT c.* FROM students_courses sc "
			+ "LEFT JOIN courses c on c.course_id = sc.course_id WHERE student_id = ?";
	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StudentDao.class);

	@Override
	public List<Student> findAll() throws DaoException {
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(GET_ALL_STUDENTS_QUERY)) {
				while (result.next()) {
					Student student = new Student();
					student.setId(result.getInt("student_id"));
					Group group = new GroupDao().getById(result.getInt("group_id"));
					student.setGroup(group);
					student.setFirstName(result.getString("first_name"));
					student.setLastName(result.getString("last_name"));
					findCoursesByStudent(student).forEach(student::setCourse);
					students.add(student);
				}
			}
			log.info("The search was successful");
		} catch (SQLException e) {
			log.error("Couldn't find all students", e);
			throw new DaoException("Couldn't find all students", e);
		}
		return students;
	}

	@Override
	public void create(Student student) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_NEW_STUDENT_QUERY,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, student.getGroup().getId());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.execute();
			try (ResultSet result = statement.getGeneratedKeys()) {
				if (result.next()) {
					student.setId(result.getInt(1));
				}
			}
		} catch (SQLException e) {
			log.error("Failed to create a student", e);
			throw new DaoException("Failed to create a student", e);
		}
	}

	@Override
	public Student getById(int studentId) throws DaoException {
		Student student = new Student();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, studentId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					student.setId(result.getInt("student_id"));
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
			throw new DaoException("Student whith id-" + studentId + " not found", e);
		}
		return student;
	}

	@Override
	public void update(Student student) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_QUERY)) {
			statement.setInt(1, student.getGroup().getId());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setInt(4, student.getId());
			statement.executeUpdate();
			log.info("Ok, student has been update");
		} catch (SQLException e) {
			log.error("Update error", e);
			throw new DaoException("Update error", e);
		}
	}

	@Override
	public void delete(Student student) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, student.getId());
			statement.execute();
			log.info("Ok, student has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
			throw new DaoException("Deletion error", e);
		}
	}

	public Set<Course> findCoursesByStudent(Student student) throws DaoException {
		Set<Course> courses = new HashSet<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_QUERY)) {
			statement.setInt(1, student.getId());
			statement.execute();
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					Course course = new Course();
					course.setId(result.getInt("course_id"));
					course.setName(result.getString("course_name"));
					course.setDescription(result.getString("course_description"));
					courses.add(course);
				}
			}
		} catch (SQLException e) {
			log.error("Error in finding courses", e);
			throw new DaoException("Error in finding courses", e);
		}
		return courses;
	}
}