package com.example.demo.mappings;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void itShouldMapTaskToTaskDTO() {
        Task task = new Task();
        task.setDescription("Write tests");
        task.setCompleted(false);

        TaskDTO dto = taskMapper.taskToTaskDTO(task);

        assertNotNull(dto);
        assertEquals(task.getDescription(), dto.getDescription());
        assertEquals(task.isCompleted(), dto.isCompleted());
    }

    @Test
    void itShouldMapTaskDTOToTask() {
        TaskDTO dto = new TaskDTO();
        dto.setDescription("Write tests");
        dto.setCompleted(true);

        Task task = taskMapper.taskDTOToTask(dto);

        assertNotNull(task);
        assertEquals(dto.getDescription(), task.getDescription());
        assertEquals(dto.isCompleted(), task.isCompleted());
    }

    @Test
    void itShouldHandleEmptyTaskToTaskDTO() {
        Task task = new Task();
        TaskDTO dto = taskMapper.taskToTaskDTO(task);

        assertNotNull(dto);
        assertNull(dto.getDescription());
        assertFalse(dto.isCompleted());
    }

    @Test
    void itShouldHandleEmptyTaskDTOToTask() {
        TaskDTO dto = new TaskDTO();
        Task task = taskMapper.taskDTOToTask(dto);

        assertNotNull(task);
        assertNull(task.getDescription());
        assertFalse(task.isCompleted());
    }
}
