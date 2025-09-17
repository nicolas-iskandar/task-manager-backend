package com.example.demo.service;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappings.TaskMapper;
import com.example.demo.models.Task;
import com.example.demo.repo.TasksRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TasksServiceTest {

    @Mock
    private TasksRepo tasksRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TasksService tasksService;

    private Task task;

    @BeforeEach
    void setup() {
        task = Task.builder()
                .id(1)
                .description("Task Description")
                .completed(false)
                .build();
    }

    @Test
    void itShouldGetTasks() {
        when(tasksRepository.findAll()).thenReturn(List.of(task));

        List<Task> returnedTask = tasksService.getTasks();

        assertThat(returnedTask).hasSize(1);
        assertThat(returnedTask).isEqualTo(List.of(task));
    }

    @Test
    void itShouldGetTaskById() {
        when(tasksRepository.findById(1)).thenReturn(Optional.ofNullable(task));

        Task returnedTask = tasksService.getTaskById(task.getId());

        assertThat(returnedTask).isEqualTo(task);
    }

    @Test
    void itShouldThrowWhenTaskNotFound() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tasksService.getTaskById(1));
    }

    @Test
    void itShouldCreateTask() {
        TaskDTO taskDTO = TaskDTO.builder()
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();

        when(tasksRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskDTOToTask(any(TaskDTO.class))).thenReturn(task);

        Task createdTask = tasksService.createTask(taskDTO);

        verify(tasksRepository).save(task);
        assertThat(createdTask).isEqualTo(task);
    }

    @Test
    void itShouldUpdateTask() {
        TaskDTO updateTaskDto = TaskDTO.builder()
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();
        Task updatedTask = Task.builder().
                description(updateTaskDto.getDescription())
                .completed(updateTaskDto.isCompleted())
                .build();

        when(tasksRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskDTOToTask(any(TaskDTO.class))).thenReturn(updatedTask);
        when(tasksRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.taskToTaskDTO(any(Task.class))).thenReturn(updateTaskDto);

        TaskDTO returnedTaskFromUpdate = tasksService.updateTask(task.getId(), updateTaskDto);

        assertThat(returnedTaskFromUpdate).isNotNull();
        assertThat(returnedTaskFromUpdate).isEqualTo(updateTaskDto);
    }

    @Test
    void itShouldThrowWhenTaskNotFoundOnUpdate() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tasksService.updateTask(1, any(TaskDTO.class)));
    }

    @Test
    void itShouldDeleteTask() {
        when(tasksRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        doNothing().when(tasksRepository).delete(task);

        tasksService.deleteTask(task.getId());

        verify(tasksRepository).delete(task);
    }

    @Test
    void itShouldThrowWhenTaskNotFoundOnDelete() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tasksService.deleteTask(1));
    }
}