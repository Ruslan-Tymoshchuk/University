package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.util.ConnectionManager;

public class CourseDao implements CrudRepository<Course> {

	public static final String GET_ALL_COURSES_QUERY = "SELECT * FROM courses";
	public static final String CREATE_COURSE_QUERY = "INSERT INTO courses(course_name, course_description) values (?, ?)";
	public static final String GET_COURSE_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
	public static final String UPDATE_COURSE_QUERY = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
	public static final String DELETE_COURSE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?";
	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CourseDao.class);

	@Override
	public List<Course> findAll() throws DaoException {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_ALL_COURSES_QUERY);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				Course course = new Course();
				course.setId(result.getInt("course_id"));
				course.setName(result.getString("course_name"));
				course.setDescription(result.getString("course_description"));
				courses.add(course);
			}
			log.info("Here are all the courses");
		} catch (SQLException e) {
			log.error("Couldn't find all courses", e);
			throw new DaoException("Couldn't find all courses", e);
		}
		return courses;
	}

	@Override
	public void create(Course course) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(CREATE_COURSE_QUERY,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, course.getName());
			statement.setString(2, course.getDescription());
			statement.execute();
			try (ResultSet result = statement.getGeneratedKeys()) {
				if (result.next()) {
					course.setId(result.getInt(1));
				}
			}
		} catch (SQLException e) {
			log.error("Failed to create a course", e);
			throw new DaoException("Failed to create a course", e);
		}
	}

	@Override
	public Course getById(int courseId) throws DaoException {
		Course course = new Course();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_COURSE_BY_ID_QUERY)) {
			statement.setInt(1, courseId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					course.setId(result.getInt("course_id"));
					course.setName(result.getString("course_name"));
					course.setDescription(result.getString("course_description"));
				}
			}
		} catch (SQLException e) {
			log.error("Сourse whith id-" + courseId + " not found", e);
			throw new DaoException("Сourse whith id-" + courseId + " not found", e);
		}
		return course;
	}

	@Override
	public void update(Course course) throws DaoException {
		try (Connection connection = connectionManager.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_QUERY)) {
				statement.setString(1, course.getName());
				statement.setString(2, course.getDescription());
				statement.setInt(3, course.getId());
				statement.executeUpdate();
				log.info("Ok, course has been update");
			}
		} catch (SQLException e) {
			log.error("Update error", e);
			throw new DaoException("Update error", e);
		}
	}

	@Override
	public void delete(Course course) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_BY_ID_QUERY)) {
			statement.setInt(1, course.getId());
			statement.execute();
			log.info("Ok, course has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
			throw new DaoException("Deletion error", e);
		}
	}
}