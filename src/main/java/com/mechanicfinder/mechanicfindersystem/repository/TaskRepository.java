package com.mechanicfinder.mechanicfindersystem.repository;

import com.mechanicfinder.mechanicfindersystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findTaskByTaskName(String taskName);
    Task findTaskById(Long id);
    List<Task> findAllByTaskName(String taskName);
    void deleteTaskById(Long id);
}
