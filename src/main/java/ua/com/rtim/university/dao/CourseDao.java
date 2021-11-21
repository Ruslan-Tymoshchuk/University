package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.rtim.university.domain.Course;

public class CourseDao extends DaoFactory<Course> {

	public static final String GET_ALL_COURSES_QUERY = "SELECT course_id FROM courses";
	public static final String CREATE_COURSE_QUERY = "INSERT INTO courses(course_name, course_description) values (?, ?)";
	public static final String GET_COURSE_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
	public static final String UPDATE_COURSE_QUERY = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
	public static final String DELETE_COURSE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?";

	@Override
	public List<Course> findAll() {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_ALL_COURSES_QUERY);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				courses.add(getById(result.getRow()));
			}
			log.info("Here are all the courses");
		} catch (SQLException e) {
			log.error("I can't find all courses", e);
		}
		return courses;
	}

	@Override
	public void create(Course course) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(CREATE_COURSE_QUERY)) {
			statement.setString(1, course.getCourseName());
			statement.setString(2, course.getCourseDescription());
			statement.execute();
			log.info("Ok, course has been added");
		} catch (SQLException e) {
			log.error("Failed to create a course", e);
		}
	}

	@Override
	public Course getById(int courseId) {
		Course course = new Course();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_COURSE_BY_ID_QUERY)) {
			statement.setInt(1, courseId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					course.setCourseId(result.getInt("course_id"));
					course.setCourseName(result.getString("course_name"));
					course.setDescription(result.getString("course_description"));
				}
			}
		} catch (SQLException e) {
			log.error("Сourse whith id-" + courseId + " not found", e);
		}
		return course;
	}

	@Override
	public void update(Course course) {
		try (Connection connection = connectionManager.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_QUERY)) {
				statement.setString(1, course.getCourseName());
				statement.setString(2, course.getCourseDescription());
				statement.setInt(3, course.getCourseId());
				statement.execute();
				log.info("Ok, course has been update");
			}
		} catch (SQLException e) {
			log.error("Update error", e);
		}
	}

	@Override
	public void delete(Course course) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_BY_ID_QUERY)) {
			statement.setInt(1, course.getCourseId());
			statement.execute();
			log.info("Ok, course has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
		}
	}
}
