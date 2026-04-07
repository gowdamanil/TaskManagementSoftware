package com.task.manager.repository;

import java.util.List;
import java.util.Optional;

import com.task.manager.model.Task;

public interface TaskRepository {

	Task save(Task task);

	Optional<Task> findById(String id);

	List<Task> findAll();

	void deleteById(String id);

	boolean existsById(String id);

}
