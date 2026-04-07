package com.task.manager.service;

import com.task.manager.enums.TaskStatus;
import com.task.manager.exception.TaskNotFoundException;
import com.task.manager.model.PaginatedResponse;
import com.task.manager.model.Task;
import com.task.manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository repository;

	public Task createTask(Task task) {
		if (task.getTitle() == null || task.getDueDate() == null) {
			throw new IllegalArgumentException("Title and Due Date are mandatory");
		}

		if (task.getDueDate() == null || !task.getDueDate().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Due Date must be in the future or not empty");
		}

		if (task.getStatus() == null) {
			task.setStatus(TaskStatus.PENDING);
		}

		task.setId(UUID.randomUUID().toString());
		return repository.save(task);
	}

	public Task getTaskById(String id) {
		return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	}

	public PaginatedResponse<Task> getAllTasks(int page, int size, TaskStatus status) {
		List<Task> filtered = repository.findAll().stream().filter(t -> status == null || t.getStatus() == status)
				.sorted(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		int total = filtered.size();
		int start = Math.min(page * size, total);
		int end = Math.min(start + size, total);

		return new PaginatedResponse<>(filtered.subList(start, end), page, size, total,
				(int) Math.ceil((double) total / size));
	}

	public Task updateTask(String id, Task updates) {
		Task task = getTaskById(id); 

		
		if (updates.getTitle() != null) {
			if (updates.getTitle().isEmpty())
				throw new IllegalArgumentException("Title cannot be empty");
			task.setTitle(updates.getTitle());
		}

		if (updates.getDescription() != null) {
			task.setDescription(updates.getDescription());
		}

		if (updates.getStatus() != null) {
			task.setStatus(updates.getStatus());
		}

		if (updates.getDueDate() != null) {
			if (!updates.getDueDate().isAfter(LocalDate.now())) {
				throw new IllegalArgumentException("New Due Date must be in the future");
			}
			task.setDueDate(updates.getDueDate());
		}

		return repository.save(task);
	}

	public void deleteTask(String id) {
		if (!repository.existsById(id))
			throw new TaskNotFoundException(id);
		repository.deleteById(id);
	}
}