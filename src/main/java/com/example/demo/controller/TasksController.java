package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.models.Task;
import com.example.demo.service.TasksService;

@RestController
@RequestMapping("/tasks")
public class TasksController {
	private final TasksService service;

	public TasksController(TasksService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Task>> getTasks() {
		return new ResponseEntity<>(service.getTasks(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
		Task task = service.getTaskById(id);
		if (task == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
		return new ResponseEntity<>(service.createTask(taskDTO), HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<TaskDTO> updateTask(@PathVariable Integer id, @RequestBody TaskDTO updatedTask) {
		Task task = service.getTaskById(id);
		if (task == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(service.updateTask(id, updatedTask), HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<TaskDTO> deleteTask(@PathVariable Integer id) {
		Task task = service.getTaskById(id);
		if (task == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		service.deleteTask(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
