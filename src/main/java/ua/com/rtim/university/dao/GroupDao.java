package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.util.ConnectionManager;

public class GroupDao implements CrudRepository<Group> {

	private static Logger log = Logger.getLogger(GroupDao.class);

	public static final String GET_ALL_GROUPS_QUERY = "SELECT * FROM groups";
	public static final String GET_ALL_GROUPS_BY_STUDENTS_AMOUNT_QUERY = "SELECT gr.*, COUNT(st.student_id) AS students_amount "
			+ "FROM groups gr LEFT JOIN students st ON st.group_id = gr.group_id "
			+ "GROUP BY gr.group_id HAVING COUNT(student_id) <= ? ORDER BY gr.group_id";
	public static final String ADD_NEW_GROUP_QUERY = "INSERT INTO groups (group_name) VALUES (?)";
	public static final String GET_GROUP_QUERY = "SELECT * FROM groups WHERE group_id = ?";
	public static final String UPDATE_GROUP_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
	public static final String DELETE_GROUP_QUERY = "DELETE FROM groups WHERE group_id = ?";

	private final ConnectionManager connectionManager;

	public GroupDao(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	@Override
	public List<Group> findAll() throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(GET_ALL_GROUPS_QUERY)) {
			List<Group> groups = new ArrayList<>();
			while (resultSet.next()) {
				groups.add(mapToGroup(resultSet));
			}
			return groups;
		} catch (SQLException e) {
			throw new DaoException("Couldn't find all groups", e);
		}
	}

	@Override
	public void create(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_NEW_GROUP_QUERY,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, group.getName());
			statement.execute();
			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				if (resultSet.next()) {
					group.setId(resultSet.getInt(1));
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Failed to create a group", e);
		}
	}

	@Override
	public Optional<Group> getById(int groupId) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_GROUP_QUERY)) {
			statement.setInt(1, groupId);
			try (ResultSet resultSet = statement.executeQuery()) {
				Optional<Group> group = Optional.empty();
				if (resultSet.next()) {
					group = Optional.of(mapToGroup(resultSet));
				}
				return group;
			}
		} catch (SQLException e) {
			throw new DaoException("group whith id-" + groupId + " not found", e);
		}
	}

	@Override
	public void update(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_GROUP_QUERY)) {
			statement.setString(1, group.getName());
			statement.setInt(2, group.getId());
			statement.executeUpdate();
			log.info("Ok, group has been update");
		} catch (SQLException e) {
			throw new DaoException("Update error", e);
		}
	}

	@Override
	public void delete(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_GROUP_QUERY)) {
			statement.setInt(1, group.getId());
			statement.execute();
			log.info("Ok, group has been deleted");
		} catch (SQLException e) {
			throw new DaoException("Deletion error", e);
		}
	}

	public List<Group> findGroupsByStudentsAmount(int amount) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_ALL_GROUPS_BY_STUDENTS_AMOUNT_QUERY)) {
			statement.setInt(1, amount);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Group> groups = new ArrayList<>();
				while (resultSet.next()) {
					groups.add(mapToGroup(resultSet));
				}
				log.info("The groups were found");
				return groups;
			}
		} catch (SQLException e) {
			throw new DaoException("Error in finding groups", e);
		}
	}

	public Group mapToGroup(ResultSet resultSet) throws SQLException {
		Group group = new Group();
		group.setId(resultSet.getInt("group_id"));
		group.setName(resultSet.getString("group_name"));
		return group;
	}
}