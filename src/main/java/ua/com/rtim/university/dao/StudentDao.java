package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ConnectionManager;

public class StudentDao implements CrudRepository<Student> {

	private static Logger log = Logger.getLogger(StudentDao.class);

	public static final String GET_ALL_STUDENTS_QUERY = "SELECT st.student_id, st.group_id, gr.group_name, st.first_name, st.last_name "
			+ "FROM students st LEFT JOIN groups gr ON gr.group_id = st.group_id";
	public static final String ADD_NEW_STUDENT_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?)";
	public static final String ADD_TO_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) values(?, ?)";
	public static final String GET_STUDENT_BY_ID_QUERY = "SELECT st.student_id, st.group_id, gr.group_name, st.first_name, st.last_name "
			+ "FROM students st LEFT JOIN groups gr ON gr.group_id = st.group_id WHERE student_id = ?";
	public static final String UPDATE_STUDENT_QUERY = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
	public static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
	public static final String FIND_STUDENTS_BY_COUSE_QUERY = "SELECT st.student_id, st.group_id, gr.group_name, "
			+ "st.first_name, st.last_name FROM courses c LEFT JOIN students_courses sc ON sc.course_id = c.course_id "
			+ "LEFT JOIN students st ON st.student_id = sc.student_id "
			+ "LEFT JOIN groups gr ON gr.group_id = st.group_id WHERE c.course_id = ?";

	private final ConnectionManager connectionManager;
	private final GroupDao groupDao;

	public StudentDao(ConnectionManager connectionManager, GroupDao groupDao) {
		this.connectionManager = connectionManager;
		this.groupDao = groupDao;
	}

	@Override
	public List<Student> findAll() throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(GET_ALL_STUDENTS_QUERY)) {
				List<Student> students = new ArrayList<>();
				while (resultSet.next()) {
					students.add(mapToStudent(resultSet));
				}
				log.info("The search was successful");
				return students;
			}
		} catch (SQLException e) {
			throw new DaoException("Couldn't find all students", e);
		}
	}

	@Override
	public void create(Student student) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement createStudent = connection.prepareStatement(ADD_NEW_STUDENT_QUERY,
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement addToCourse = connection.prepareStatement(ADD_TO_COURSE_QUERY)) {
			connection.setAutoCommit(false);
			createStudent.setObject(1, student.getGroup().getId(), Types.INTEGER);
			createStudent.setString(2, student.getFirstName());
			createStudent.setString(3, student.getLastName());
			createStudent.execute();
			try (ResultSet resultSet = createStudent.getGeneratedKeys()) {
				Set<Course> studentCourses = student.getCourses();
				if (resultSet.next() && !studentCourses.isEmpty()) {
					int studentId = resultSet.getInt(1);
					student.setId(studentId);
					for (Course course : studentCourses) {
						addToCourse.setInt(1, studentId);
						addToCourse.setInt(2, course.getId());
						addToCourse.execute();
					}
					connection.commit();
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Failed to create a student", e);
		}
	}

	@Override
	public Optional<Student> getById(int id) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				Optional<Student> student = Optional.empty();
				if (resultSet.next()) {
					student = Optional.of(mapToStudent(resultSet));
				}
				return student;
			}
		} catch (SQLException e) {
			throw new DaoException("Student whith id-" + id + " not found", e);
		}
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
			throw new DaoException("Deletion error", e);
		}
	}

	public void addToCourse(int studentId, int courseId) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_TO_COURSE_QUERY)) {
			statement.setInt(1, studentId);
			statement.setInt(2, courseId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Error adding a student to a course", e);
		}
	}

	public List<Student> findAllStudentsByCourse(int id) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_STUDENTS_BY_COUSE_QUERY)) {
			statement.setInt(1, id);
			statement.execute();
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Student> students = new ArrayList<>();
				while (resultSet.next()) {
					students.add(mapToStudent(resultSet));
				}
				return students;
			}
		} catch (SQLException e) {
			throw new DaoException("Error in finding courses", e);
		}
	}

	public Student mapToStudent(ResultSet resultSet) throws SQLException {
		Student student = new Student();
		student.setId(resultSet.getInt("student_id"));
		Group group = groupDao.mapToGroup(resultSet);
		student.setGroup(group);
		student.setFirstName(resultSet.getString("first_name"));
		student.setLastName(resultSet.getString("last_name"));
		return student;
	}
}