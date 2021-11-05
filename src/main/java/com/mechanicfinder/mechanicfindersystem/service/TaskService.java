package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Task;

import java.util.List;

public interface TaskService {
    Task findTaskByTaskName(String taskName);
    List<Task> findAllTasks();
}
