package ua.com.rtim.university.ui;

import java.util.List;

public interface CrudRepository<T> {

	List<T> findAll();

	void create(T t);

	T getById(int id);

	void update(T t);

	void delete(T t);

}




	



