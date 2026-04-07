package com.task.manager.controller;

import com.task.manager.exception.TaskNotFoundException;
import com.task.manager.model.Task;
import com.task.manager.service.TaskService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean // Note: Use @MockitoBean if you are on Spring Boot 3.4+
	private TaskService taskService;

	@Test
	void shouldReturn201WhenCreatingTask() throws Exception {
		Task mockTask = Task.builder().id("123").title("API Test").build();
		when(taskService.createTask(any())).thenReturn(mockTask);

		mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"API Test\",\"dueDate\":\"2026-12-31\"}")).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value("123"));
	}

	@Test
	void shouldReturn404WhenTaskNotFound() throws Exception {
		when(taskService.getTaskById("99")).thenThrow(new com.task.manager.exception.TaskNotFoundException("99"));

		mockMvc.perform(get("/api/v1/tasks/99")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturn204WhenDeletingTask() throws Exception {
		mockMvc.perform(delete("/api/v1/tasks/123")).andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Should return consistent error format for 404")
	void shouldReturnStructuredError() throws Exception {
	    when(taskService.getTaskById("any")).thenThrow(new TaskNotFoundException("Task not found"));

	    mockMvc.perform(get("/api/v1/tasks/any"))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.message").exists())
	            .andExpect(jsonPath("$.timestamp").exists());
	}
}