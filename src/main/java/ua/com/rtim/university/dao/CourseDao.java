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

import org.apache.log4j.Logger;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ConnectionManager;

public class CourseDao implements CrudRepository<Course> {

	private static Logger log = Logger.getLogger(CourseDao.class);
	public static final String GET_ALL_COURSES_QUERY = "SELECT * FROM courses";
	public static final String CREATE_COURSE_QUERY = "INSERT INTO courses(course_name, course_description) values (?, ?)";
	public static final String GET_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
	public static final String UPDATE_COURSE_QUERY = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
	public static final String REMOVE_COURSE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?";
	public static final String REMOVE_FROM_COURSE_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
	public static final String FIND_COURSES_BY_STUDENT_QUERY = "SELECT c.* FROM students_courses sc "
			+ "LEFT JOIN courses c on c.course_id = sc.course_id WHERE student_id = ?";
	private final ConnectionManager connectionManager;
	private final StudentDao studentDao;

	public CourseDao(ConnectionManager connectionManager, StudentDao studentDao) {
		this.connectionManager = connectionManager;
		this.studentDao = studentDao;
	}

	@Override
	public List<Course> findAll() throws DaoException {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_ALL_COURSES_QUERY);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				courses.add(mapToCourse(resultSet));
			}
			log.info("Here are all the courses");
		} catch (SQLException e) {
			throw new DaoException("Couldn't find all courses", e);
		}
		return courses;
	}

	@Override
	public void create(Course course) throws DaoException {
		if (course.getStudent() != null) {
			studentDao.addToCourse(course.getStudent(), course);
		} else {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(CREATE_COURSE_QUERY,
							Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, course.getName());
				statement.setString(2, course.getDescription());
				statement.execute();
				try (ResultSet resultSet = statement.getGeneratedKeys()) {
					if (resultSet.next()) {
						course.setId(resultSet.getInt(1));
					}
				}
			} catch (SQLException e) {
				throw new DaoException("Failed to create a course", e);
			}
		}
	}

	@Override
	public Course getById(int courseId) throws DaoException {
		Course course = new Course();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
			statement.setInt(1, courseId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					course = mapToCourse(resultSet);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Сourse whith id-" + courseId + " not found", e);
		}
		return course;
	}

	@Override
	public void update(Course course) throws DaoException {
		try (Connection connection = connectionManager.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_QUERY)) {
				statement.setInt(1, course.getId());
				statement.setString(2, course.getName());
				statement.setString(3, course.getDescription());
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DaoException("Update error", e);
		}
	}

	@Override
	public void delete(Course course) throws DaoException {
		if (course.getStudent() != null) {
			removeFromCourse(course.getStudent(), course);
		} else {
			try (Connection connection = connectionManager.getConnection();
					PreparedStatement statement = connection.prepareStatement(REMOVE_COURSE_BY_ID_QUERY)) {
				statement.setInt(1, course.getId());
				statement.execute();
			} catch (SQLException e) {
				throw new DaoException("Deletion error", e);
			}
		}
	}

	private void removeFromCourse(Student student, Course course) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(REMOVE_FROM_COURSE_QUERY)) {
			statement.setInt(1, student.getId());
			statement.setInt(2, course.getId());
			statement.execute();
		} catch (SQLException e) {
			log.error("Error when deleting from the course", e);
			throw new DaoException("Error when deleting from the course", e);
		}
	}

	public Set<Course> findCoursesByStudent(Student student) throws DaoException {
		Set<Course> courses = new HashSet<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_QUERY)) {
			statement.setInt(1, student.getId());
			statement.execute();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					courses.add(mapToCourse(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Error in finding courses", e);
		}
		return courses;
	}

	private Course mapToCourse(ResultSet resultSet) throws SQLException, DaoException {
		Course course = new Course();
		course.setId(resultSet.getInt("course_id"));
		course.setName(resultSet.getString("course_name"));
		course.setDescription(resultSet.getString("course_description"));
		List<Student> studentsByCourse = studentDao.findAllStudentsByCourse(course);
		course.setStudents(studentsByCourse);
		return course;
	}
}