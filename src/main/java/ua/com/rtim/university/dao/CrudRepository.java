package ua.com.rtim.university.dao;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

	List<T> findAll() throws DaoException;

	void create(T entity) throws DaoException;

	Optional<T> getById(int id) throws DaoException;

	void update(T entity) throws DaoException;

	void delete(T entity) throws DaoException;

}