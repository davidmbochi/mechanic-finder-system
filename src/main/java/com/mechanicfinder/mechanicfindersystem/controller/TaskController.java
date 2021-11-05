package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/task")
@RestController
@RequiredArgsConstructor
public class TaskController{

    private final TaskService taskService;

    @GetMapping("/all")
    public List<Task> findAllTasks(){
        return taskService.findAllTasks();
    }
}
