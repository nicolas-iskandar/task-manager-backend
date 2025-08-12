package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.TaskDTO;
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

	public Task getTaskById(int id) {
		Task task = repo.findById(id).orElse(null);
		if (task == null)
			return null;
		return task;
	}

	public List<Task> getTasks() {
		return repo.findAll();
	}

	public Task createTask(TaskDTO taskDTO) {
		Task task = mapper.taskDTOToTask(taskDTO);
		return repo.save(task);
	}

	public TaskDTO updateTask(int id, TaskDTO updatedTask) {
		Task task = mapper.taskDTOToTask(updatedTask);
		task.setId(id);
		return mapper.taskToTaskDTO(repo.save(task));
	}

	public Task deleteTask(int id) {
		Task task = repo.findById(id).orElse(null);
		if (task == null)
			return null;

		repo.deleteById(id);
		return task;
	}
}
