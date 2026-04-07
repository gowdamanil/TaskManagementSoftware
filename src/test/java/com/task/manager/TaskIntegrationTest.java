package com.task.manager;

import com.task.manager.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCompleteTaskFlow() {
        // 1. Create a Task
        Task newTask = Task.builder()
                .title("Integration Test Task")
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        ResponseEntity<Task> postResponse = restTemplate.postForEntity("/api/v1/tasks", newTask, Task.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        String taskId = postResponse.getBody().getId();

        // 2. Update the Task status
        Task updateRequest = Task.builder().status(com.task.manager.enums.TaskStatus.IN_PROGRESS).build();
        restTemplate.put("/api/v1/tasks/" + taskId, updateRequest);

        // 3. Verify Update via Get
        ResponseEntity<Task> getResponse = restTemplate.getForEntity("/api/v1/tasks/" + taskId, Task.class);
        assertEquals(com.task.manager.enums.TaskStatus.IN_PROGRESS, getResponse.getBody().getStatus());

        // 4. Delete the Task
        restTemplate.delete("/api/v1/tasks/" + taskId);

        // 5. Verify 404 after deletion
        ResponseEntity<Map> finalCheck = restTemplate.getForEntity("/api/v1/tasks/" + taskId, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, finalCheck.getStatusCode());
    }
}