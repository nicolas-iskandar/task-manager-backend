package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Task;

public interface TasksRepo extends JpaRepository<Task, Integer> {
}
