package com.example.demo.repo;

import com.example.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class TasksRepoTest {

    @Autowired
    private TasksRepo tasksRepository;

    private Task task;

    @BeforeEach
    void setup() {
        task = Task.builder()
                .description("Task Description")
                .completed(false)
                .build();
    }

    @Test
    void itShouldReturnSavedTask() {
        tasksRepository.save(task);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isGreaterThan(0);
    }

    @Test
    void itShouldReturnTasks() {
        tasksRepository.save(task);
        tasksRepository.save(Task.builder()
                .description("Another Task")
                .completed(false)
                .build());

        List<Task> tasks = tasksRepository.findAll();

        assertThat(tasks).isNotNull();
        assertThat(tasks).hasSize(2);
    }

    @Test
    void itShouldReturnSingleTask() {
        tasksRepository.save(task);

        Task returnedTask = tasksRepository.findById(task.getId()).orElse(null);

        assertThat(returnedTask).isNotNull();
        assertThat(returnedTask.getId()).isEqualTo(task.getId());
    }

    @Test
    void itShouldReturnUpdatedTask() {
        tasksRepository.save(task);

        Task returnedTask = tasksRepository.findById(task.getId()).orElse(null);
        assertThat(returnedTask).isNotNull();

        String newDescription = "New Description";
        returnedTask.setDescription(newDescription);
        returnedTask.setCompleted(true);

        Task updatedTask = tasksRepository.save(returnedTask);

        assertEquals(newDescription, updatedTask.getDescription());
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    void itShouldReturnEmptyWhenTaskIsDeleted() {
        tasksRepository.save(task);

        tasksRepository.deleteById(task.getId());
        Optional<Task> returnedTask = tasksRepository.findById(task.getId());

        assertThat(returnedTask).isEmpty();
    }
}