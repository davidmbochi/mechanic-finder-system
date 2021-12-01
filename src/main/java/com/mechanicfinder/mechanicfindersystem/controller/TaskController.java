package com.mechanicfinder.mechanicfindersystem.controller;
import com.mechanicfinder.mechanicfindersystem.exception.TaskWithTheProvidedNameExists;
import com.mechanicfinder.mechanicfindersystem.model.AppUser;
import com.mechanicfinder.mechanicfindersystem.model.ApplicationStatus;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.service.AppUserService;
import com.mechanicfinder.mechanicfindersystem.service.MechanicService;
import com.mechanicfinder.mechanicfindersystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/task")
@Controller
@RequiredArgsConstructor
public class TaskController{

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final MechanicService mechanicService;
    private final AppUserService appUserService;

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

    @GetMapping("/task-add-form/{id}")
    public String addTaskForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("mechanic",mechanicService.findMechanicById(id));
        model.addAttribute("tasks",taskService.findAllTasks());
        model.addAttribute("task",new Task());
        return "task-views/task-add-form";
    }


    @PostMapping("/process-add-task/{id}")
    public String processAddTask(@RequestParam("taskName") String taskName,
                                 @PathVariable("id")Long id) {
        Mechanic mechanic = mechanicService.findMechanicById(id);
        Task task = taskService.findTaskByTaskName(taskName);
        taskService.addTask(mechanic.getId(), task);
        return "redirect:/api/mechanic/"+mechanic.getId();
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

        Task task = taskService.findTaskByTaskName(taskName);

        List<Mechanic> approvedMechanics = task.getMechanics()
                .stream()
                .filter(mechanic -> mechanic.getApplicationStatus()
                        .equals(ApplicationStatus.MEMBER.toString()))
                .collect(Collectors.toList());

        model.addAttribute("mechanics",approvedMechanics);

        model.addAttribute("taskName",task.getTaskName());

        return "mechanic-views/mechanic-task-viewport";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable("id") Long id, Model model){
        model.addAttribute("task", taskService.findTaskById(id));
        return "task-views/edit-task";
    }

    @PostMapping("/process-update")
    public String processUpdate(@ModelAttribute("task") Task task){
        Task updatedTask = taskService.updateTask(task);

        Mechanic mechanic = getLoggedInMechanic();


        return "redirect:/api/mechanic/"+mechanic.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        Task task = taskService.findTaskById(id);
        logger.info("Task ==========> "+task);
        taskService.deleteTask(task);
        Mechanic mechanic = getLoggedInMechanic();
        return "redirect:/api/mechanic/"+mechanic.getId();
    }

    private Mechanic getLoggedInMechanic() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser appUserByUserName = appUserService.findAppUserByUserName(name);

        return appUserByUserName.getMechanic();
    }

}
