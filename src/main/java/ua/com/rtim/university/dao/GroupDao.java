package ua.com.rtim.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.rtim.university.domain.Group;

public class GroupDao extends DaoFactory<Group> {

	public static final String GET_ALL_GROUPS_QUERY = "SELECT group_id FROM groups";
	public static final String ADD_NEW_GROUP_QUERY = "INSERT INTO groups (group_name) VALUES (?)";
	public static final String GET_GROUP_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
	public static final String UPDATE_GROUP_BY_ID_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
	public static final String DELETE_GROUP_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?";

	@Override
	public List<Group> findAll() {
		List<Group> groups = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(GET_ALL_GROUPS_QUERY)) {
			while (result.next()) {
				groups.add(getById(result.getRow()));
			}
			log.info("The search was successful");
		} catch (SQLException e) {
			log.error("I can't find all groups", e);
		}
		return groups;
	}

	@Override
	public void create(Group group) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(ADD_NEW_GROUP_QUERY)) {
			statement.setString(1, group.getGroupName());
			statement.execute();
			log.info("Ok, group has been added");
		} catch (SQLException e) {
			log.error("Failed to create a group", e);
		}
	}

	@Override
	public Group getById(int groupId) {
		Group group = new Group();
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(GET_GROUP_BY_ID_QUERY)) {
			statement.setInt(1, groupId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					group.setGroupId((result.getInt("group_id")));
					group.setGroupName(result.getString("group_name"));
				}
			}
		} catch (SQLException e) {
			log.error("group whith id-" + groupId + " not found", e);
		}
		return group;
	}

	@Override
	public void update(Group group) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_GROUP_BY_ID_QUERY)) {
			statement.setString(1, group.getGroupName());
			statement.setInt(2, group.getGroupId());
			statement.execute();
			log.info("Ok, group has been update");
		} catch (SQLException e) {
			log.error("Update error", e);
		}
	}

	@Override
	public void delete(Group group) {
		try (Connection connection = connectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_GROUP_BY_ID_QUERY)) {
			statement.setInt(1, group.getGroupId());
			statement.execute();
			log.info("Ok, group has been deleted");
		} catch (SQLException e) {
			log.error("Deletion error", e);
		}
	}
}
