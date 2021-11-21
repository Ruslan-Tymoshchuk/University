package ua.com.rtim.university.dao;

import ua.com.rtim.university.ui.CrudRepository;
import ua.com.rtim.university.util.ConnectionManager;

public abstract class DaoFactory<T> implements CrudRepository<T> {

	protected ConnectionManager connectionManager = new ConnectionManager();
	protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DaoFactory.class);

}
