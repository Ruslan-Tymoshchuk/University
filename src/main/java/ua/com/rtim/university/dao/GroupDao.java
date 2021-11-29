package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.util.ConnectionManager;

public class GroupDao implements CrudRepository<Group> {

	public static final String GET_ALL_GROUPS_QUERY = "SELECT * FROM groups";
	public static final String ADD_NEW_GROUP_QUERY = "INSERT INTO groups (group_name) VALUES (?)";
	public static final String GET_GROUP_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
	public static final String UPDATE_GROUP_BY_ID_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
	public static final String DELETE_GROUP_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?";
	private ConnectionManager connectionManager = new ConnectionManager();
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GroupDao.class);

	@Override
	public List<Group> findAll() throws DaoException {
		List<Group> groups = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(GET_ALL_GROUPS_QUERY)) {
			while (result.next()) {
				Group group = new Group();
				group.setId(result.getInt("group_id"));
				group.setName(result.getString("group_name"));
				groups.add(group);
			}
		} catch (SQLException e) {
			log.error("Couldn't find all groups", e);
			throw new DaoException("Couldn't find all groups", e);
		}
		return groups;
	}

	@Override
	public void create(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_NEW_GROUP_QUERY,
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, group.getName());
			statement.execute();
			try (ResultSet result = statement.getGeneratedKeys()) {
				if (result.next()) {
					group.setId(result.getInt(1));
				}
			}
		} catch (SQLException e) {
			log.error("Failed to create a group", e);
			throw new DaoException("Failed to create a group", e);
		}
	}

	@Override
	public Group getById(int groupId) throws DaoException {
		Group group = new Group();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_GROUP_BY_ID_QUERY)) {
			statement.setInt(1, groupId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					group.setId((result.getInt("group_id")));
					group.setName(result.getString("group_name"));
				}
			}
		} catch (SQLException e) {
			log.error("group whith id-" + groupId + " not found", e);
			throw new DaoException("group whith id-" + groupId + " not found", e);
		}
		return group;
	}

	@Override
	public void update(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_GROUP_BY_ID_QUERY)) {
			statement.setString(1, group.getName());
			statement.setInt(2, group.getId());
			statement.executeUpdate();
			log.info("Ok, group has been update");
		} catch (SQLException e) {
			log.error("Update error", e);
			throw new DaoException("Update error", e);
		}
	}

	@Override
	public void delete(Group group) throws DaoException {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_GROUP_BY_ID_QUERY)) {
			statement.setInt(1, group.getId());
			statement.execute();
			log.info("Ok, group has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
			throw new DaoException("Deletion error", e);
		}
	}
}