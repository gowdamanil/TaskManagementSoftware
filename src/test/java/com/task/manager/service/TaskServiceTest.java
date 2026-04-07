package com.task.manager.service;

import com.task.manager.enums.TaskStatus;
import com.task.manager.exception.TaskNotFoundException;
import com.task.manager.model.PaginatedResponse;
import com.task.manager.model.Task;
import com.task.manager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create task with generated ID and default PENDING status")
    void testCreateTask_Success() {
        Task input = Task.builder().title("Test").dueDate(LocalDate.now().plusDays(1)).build();
        when(repository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        Task result = taskService.createTask(input);

        assertNotNull(result.getId());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw exception when due date is in the past")
    void testCreateTask_InvalidDate() {
        Task input = Task.builder().title("Test").dueDate(LocalDate.now().minusDays(1)).build();
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(input));
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when ID doesn't exist")
    void testGetTask_NotFound() {
        when(repository.findById("invalid")).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById("invalid"));
    }

    @Test
    @DisplayName("Should filter tasks by status and return paginated data")
    void testGetAllTasks_Filtering() {
        Task t1 = Task.builder().status(TaskStatus.DONE).build();
        when(repository.findAll()).thenReturn(List.of(t1));

        PaginatedResponse<Task> response = taskService.getAllTasks(0, 10, TaskStatus.DONE);

        assertEquals(1, response.getData().size());
        assertEquals(TaskStatus.DONE, response.getData().get(0).getStatus());
    }
}