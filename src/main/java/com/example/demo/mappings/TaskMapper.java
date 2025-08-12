package com.example.demo.mappings;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.models.Task;

@Component
public class TaskMapper {
	public TaskDTO taskToTaskDTO(Task task) {
		return new TaskDTO(task.getDescription(), task.isCompleted());
	}

	public Task taskDTOToTask(TaskDTO taskDTO) {
		Task task = new Task();
		task.setDescription(taskDTO.getDescription());
		task.setCompleted(taskDTO.isCompleted());
		return task;
	}
}
