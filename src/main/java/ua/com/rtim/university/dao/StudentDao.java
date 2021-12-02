package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;
import ua.com.rtim.university.util.ConnectionManager;

public class StudentDao implements CrudRepository<Student> {

	public static final String GET_ALL_STUDENTS_QUERY = "SELECT st.student_id, st.group_id, gr.group_name, st.first_name, st.last_name "
			+ "FROM students st LEFT JOIN groups gr ON gr.group_id = st.group_id";
	public static final String ADD_NEW_STUDENT_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?)";
	public static final String GET_STUDENT_BY_ID_QUERY = "SELECT st.student_id, st.group_id, gr.group_name, st.first_name, st.last_name "
			+ "FROM students st LEFT JOIN groups gr ON gr.group_id = st.group_id WHERE student_id = ?";
	public static final String UPDATE_STUDENT_QUERY = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
	public static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
	public static final String GET_BY_COURSE_QUERY = "SELECT s.* "
			+ "FROM students_courses sc LEFT JOIN students s on s.student_id = sc.student_id "
			+ "LEFT JOIN courses c on c.course_id = sc.course_id WHERE course_name = ?";
	public static final String ADD_TO_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) values(?, ?)";
	public static final String REMOVE_FROM_COURSE_BY_ID_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
	public static final String FIND_STUDENTS_BY_COUSE_QUERY = "SELECT st.student_id, gr.*, st.first_name, st.last_name FROM courses c "
			+ "LEFT JOIN students_courses sc ON sc.course_id = c.course_id "
			+ "LEFT JOIN students st ON st.student_id = sc.student_id "
			+ "LEFT JOIN groups gr ON gr.group_id = st.group_id WHERE c.course_id = ?";
	private ConnectionManager connectionManager = new ConnectionManager();
	private static Logger log = Logger.getLogger(StudentDao.class);

	@Override
	public List<Student> findAll() throws DaoException {
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(GET_ALL_STUDENTS_QUERY)) {
				while (resultSet.next()) {
					students.add(mapToStudent(resultSet));
				}
			}
			log.info("The search was successful");
		} catch (SQLException e) {
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
					student.getCourses().forEach(course -> {
						course.setStudent(student);
						try {
							new CourseDao().update(course);
						} catch (DaoException e) {
							e.printStackTrace();
						}
					});
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Failed to create a student", e);
		}
	}

	@Override
	public Student getById(int studentId) throws DaoException {
		Student student = new Student();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_STUDENT_BY_ID_QUERY)) {
			statement.setInt(1, studentId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					student = mapToStudent(resultSet);
				}
			}
		} catch (SQLException e) {
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
			student.getCourses().forEach(course -> {
				course.setStudent(student);
				try {
					new CourseDao().update(course);
				} catch (DaoException e) {
					e.printStackTrace();
				}
			});
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

	public List<Student> findAllStudentsByCourseName(Course course) throws DaoException {
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_STUDENTS_BY_COUSE_QUERY)) {
			statement.setInt(1, course.getId());
			statement.execute();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					students.add(mapToStudent(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Error in finding courses", e);
		}
		return students;
	}

	public Student mapToStudent(ResultSet resultSet) throws DaoException {
		Student student = new Student();
		try {
			student.setId(resultSet.getInt("student_id"));
			Group group = new GroupDao().mapToGroup(resultSet);
			student.setGroup(group);
			student.setFirstName(resultSet.getString("first_name"));
			student.setLastName(resultSet.getString("last_name"));
			Set<Course> courses = new CourseDao().findCoursesByStudent(student);
			courses.forEach(student::setCourse);
		} catch (SQLException e) {
			throw new DaoException("Failed map to student", e);
		}
		return student;
	}
}