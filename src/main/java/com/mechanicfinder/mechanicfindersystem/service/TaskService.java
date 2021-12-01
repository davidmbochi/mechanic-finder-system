package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Task;

import java.util.List;

public interface TaskService {
    Task findTaskByTaskName(String taskName);
    List<Task> findAllTasks();
    Task addTask(Long mechanicId,Task task);
    Task findTaskById(Long id);
    Task updateTask(Task task);
    void deleteTask(Task task);
}
