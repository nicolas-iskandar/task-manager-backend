package com.example.demo.service;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappings.TaskMapper;
import com.example.demo.models.Task;
import com.example.demo.repo.TasksRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TasksService {
    private final TasksRepo repo;
    private final TaskMapper mapper;

    @Cacheable(value = "tasksList")
    public List<Task> getTasks() {
        return repo.findAll();
    }

    @Cacheable(value = "tasks", key = "#id")
    public Task getTaskById(int id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
    }

    @CacheEvict(value = "tasksList", allEntries = true)
    public Task createTask(TaskDTO taskDTO) {
        Task task = mapper.taskDTOToTask(taskDTO);
        return repo.save(task);
    }

    @Caching(evict = {
            @CacheEvict(value = "tasksList", allEntries = true),
            @CacheEvict(value = "tasks", key = "#id")
    })
    public TaskDTO updateTask(int id, TaskDTO updatedTaskDTO) {
        repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
        Task updatedTask = mapper.taskDTOToTask(updatedTaskDTO);
        updatedTask.setId(id);
        return mapper.taskToTaskDTO(repo.save(updatedTask));
    }

    @Caching(evict = {
            @CacheEvict(value = "tasksList", allEntries = true),
            @CacheEvict(value = "tasks", key = "#id")
    })
    public void deleteTask(int id) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
        repo.delete(task);
    }
}
