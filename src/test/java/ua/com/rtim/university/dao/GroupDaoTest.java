package ua.com.rtim.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.util.ScriptRunner;

class GroupDaoTest {

	private GroupDao groupDao = new GroupDao();
	private ScriptRunner scriptRunner = new ScriptRunner();

	@BeforeEach
	void generateTestData_beforeEachTest_thenTheTestResult() {
		scriptRunner.generateDatabaseData("tables.sql");
		scriptRunner.generateDatabaseData("groupdata.sql");
	}

	@Test
	void findAll_shouldBeGetAllEntities_fromTheDataBase() {
		List<Group> expected = new ArrayList<>();
		Group group1 = new Group();
		group1.setGroupId(1);
		group1.setGroupName("HJ-23");
		expected.add(group1);
		Group group2 = new Group();
		group2.setGroupId(2);
		group2.setGroupName("KM-15");
		expected.add(group2);
		Group group3 = new Group();
		group3.setGroupId(3);
		group3.setGroupName("BG-43");
		expected.add(group3);
		List<Group> gr = groupDao.findAll();
		assertEquals(expected, gr);
	}

	@Test
	void create_shouldBeAddNewEntity_intoTheDataBase() {
		Group expected = new Group();
		expected.setGroupId(4);
		expected.setGroupName("HK-23");
		groupDao.create(expected);
		Group actual = groupDao.getById(4);
		assertEquals(expected, actual);
	}

	@Test
	void update_shouldBeUpdateEntity_inTheDataBase() {
		Group expected = groupDao.getById(1);
		expected.setGroupName("TestName");
		groupDao.update(expected);
		assertEquals(expected, groupDao.getById(1));
	}

	@Test
	void delete_shouldBeRemoveEntity_fromTheDataBase() {
		groupDao.delete(groupDao.getById(1));
		Group expected = new Group();
		assertEquals(expected, groupDao.getById(1));
	}

	@Test
	void givenNull_whenCreateGroup_thenException() {
		assertThrows(NullPointerException.class, () -> groupDao.create(null));
	}

	@Test
	void givenNull_whenUpdateGroup_thenException() {
		assertThrows(NullPointerException.class, () -> groupDao.update(null));
	}

	@Test
	void givenNull_whenDeleteGroup_thenException() {
		assertThrows(NullPointerException.class, () -> groupDao.delete(null));
	}
}
