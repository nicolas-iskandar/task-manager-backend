package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappings.TaskMapper;
import com.example.demo.models.Task;
import com.example.demo.repo.TasksRepo;

@Service
public class TasksService {
	private final TasksRepo repo;
	private final TaskMapper mapper;

	public TasksService(TasksRepo repo, TaskMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	public List<Task> getTasks() {
		return repo.findAll();
	}

	public Task getTaskById(int id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
	}

	public Task createTask(TaskDTO taskDTO) {
		Task task = mapper.taskDTOToTask(taskDTO);
		return repo.save(task);
	}

	public TaskDTO updateTask(int id, TaskDTO updatedTaskDTO) {
		repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
		Task updatedTask = mapper.taskDTOToTask(updatedTaskDTO);
		updatedTask.setId(id);
		return mapper.taskToTaskDTO(repo.save(updatedTask));
	}

	public void deleteTask(int id) {
		Task task = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
		repo.delete(task);
	}
}
