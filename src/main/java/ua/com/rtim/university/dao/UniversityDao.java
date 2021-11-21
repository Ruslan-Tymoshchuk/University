package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.ui.UniversityManager;
import ua.com.rtim.university.util.ConnectionManager;

public class UniversityDao implements UniversityManager {

	public static final String GET_GROUPS_BY_STUDENTS_AMOUNT_QUERY = "SELECT st.group_id, COUNT(group_id) as group_id "
			+ "FROM students st GROUP BY st.group_id HAVING COUNT(student_id) >= ?";
	public static final String GET_BY_COURSE_QUERY = "SELECT s.student_id "
			+ "FROM students_courses sc LEFT JOIN students s on s.student_id = sc.student_id "
			+ "LEFT JOIN courses c on c.course_id = sc.course_id WHERE course_name = ?";
	public static final String ADD_TO_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) values(?, ?)";
	public static final String REMOVE_FROM_COURSE_BY_ID_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UniversityDao.class);

	@Override
	public List<Group> getGroupsByStudentAmount(int amount) {
		List<Group> groups = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_GROUPS_BY_STUDENTS_AMOUNT_QUERY)) {
			statement.setInt(1, amount);
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					groups.add(new GroupDao().getById(result.getInt("group_id")));
				}
			}
			log.info("The groups were found");
		} catch (SQLException e) {
			log.error("Error in finding groups", e);
		}
		return groups;
	}

	@Override
	public List<Student> findAllStudentsByCourseName(String courseName) {
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_BY_COURSE_QUERY)) {
			statement.setString(1, courseName);
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					students.add(new StudentDao().getById(result.getInt("student_id")));
				}
			}
			if (!students.isEmpty()) {
				log.info("The students were found");
			}
		} catch (SQLException e) {
			log.error("Error in finding students", e);
		}
		return students;
	}

	@Override
	public void addToCourse(Student student, Course course) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_TO_COURSE_QUERY)) {
			statement.setInt(1, student.getStudentId());
			statement.setInt(2, course.getCourseId());
			statement.executeUpdate();
			log.info("Ok, the student has been added to the course");
		} catch (SQLException e) {
			log.error("Error adding a student to a group", e);
		}
	}

	@Override
	public void removeFromCourse(Student student, Course course) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(REMOVE_FROM_COURSE_BY_ID_QUERY)) {
			statement.setInt(1, student.getStudentId());
			statement.setInt(2, course.getCourseId());
			statement.execute();
			log.info("The student has been removed from the course");
		} catch (SQLException e) {
			log.error("Error when deleting from the course", e);
		}
	}
}
