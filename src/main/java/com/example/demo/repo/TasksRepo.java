package com.example.demo.repo;

import com.example.demo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepo extends JpaRepository<Task, Integer> {
}
