package com.task.manager.repository;

import com.task.manager.model.Task;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    // ConcurrentHashMap is thread-safe, better for "Senior" level code
    private final Map<String, Task> storage = new ConcurrentHashMap<>();

    public Task save(Task task) {
        storage.put(task.getId(), task);
        return task;
    }

    public Optional<Task> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(String id) {
        storage.remove(id);
    }

    public boolean existsById(String id) {
        return storage.containsKey(id);
    }
}