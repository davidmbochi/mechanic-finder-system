package com.mechanicfinder.mechanicfindersystem.controller;

import com.mechanicfinder.mechanicfindersystem.exception.TaskWithTheProvidedNameExists;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import com.mechanicfinder.mechanicfindersystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/task")
@Controller
@RequiredArgsConstructor
public class TaskController{

    private final TaskService taskService;
    private final MechanicService mechanicService;

    @GetMapping("/all")
    public String findAllTasks(Model model){
        model.addAttribute("tasks",taskService.findAllTasks());
        return "task-views/all-tasks";
    }

    @GetMapping("/task-form/{id}")
    public String taskForm(@PathVariable("id")Long id, Model model){
        model.addAttribute("mechanic",mechanicService.findMechanicById(id));
        model.addAttribute("task",new Task());
        return "task-views/task-form";
    }

    @PostMapping("/processTaskForm/{id}")
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          @PathVariable("id") Long id,
                          BindingResult bindingResult,
                          Model model) throws TaskWithTheProvidedNameExists {
        if (bindingResult.hasErrors()){
            return "task-views/task-form";
        }else {
            Task addedTask = taskService.addTask(id, task);

            return "redirect:/api/mechanic/"+id;
        }

    }

    @GetMapping("/{taskName}")
    public String findTaskByTaskName(@PathVariable("taskName") String taskName,
                                  Model model){

        Task searchedTask = taskService.findTaskByTaskName(taskName);

        model.addAttribute("mechanics",searchedTask.getMechanics());

        model.addAttribute("taskName",searchedTask.getTaskName());

        return "mechanic-views/mechanic-task-viewport";
    }


}
