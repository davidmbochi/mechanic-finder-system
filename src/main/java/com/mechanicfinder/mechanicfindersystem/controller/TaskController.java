package com.mechanicfinder.mechanicfindersystem.controller;
import com.mechanicfinder.mechanicfindersystem.exception.DeleteTaskException;
import com.mechanicfinder.mechanicfindersystem.exception.MultipleTasksException;
import com.mechanicfinder.mechanicfindersystem.exception.TaskWithTheProvidedNameExists;
import com.mechanicfinder.mechanicfindersystem.model.*;
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
import java.util.stream.Stream;

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
                                 @PathVariable("id")Long id) throws MultipleTasksException {
        Mechanic mechanic = mechanicService.findMechanicById(id);
        Task task = taskService.findTaskByTaskName(taskName);

        List<Task> tasks = new ArrayList<>();
        for (Task mechanicTask : mechanic.getTasks()) {
            if (mechanicTask.getTaskName().equals(task.getTaskName())){
                tasks.add(mechanicTask);
            }
        }

        if (! tasks.isEmpty()){
            throw new MultipleTasksException("This tasks is already present");
        }else {
            if (taskName.isEmpty()){
                return "task-views/task-add-form";
            }else {
                taskService.addTask(mechanic.getId(), task);
                return "redirect:/api/mechanic/"+mechanic.getId();
            }
        }
    }

    @PostMapping("/processTaskForm/{id}")
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          BindingResult bindingResult,
                          @PathVariable("id") Long id,
                          Model model) throws TaskWithTheProvidedNameExists {
        if (bindingResult.hasErrors()){
            model.addAttribute("mechanic", mechanicService.findMechanicById(id));
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
                .filter(mechanic -> mechanic.getStringApplicationStatus()
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

    @PostMapping("/process-update/{id}")
    public String processUpdate(@Valid @ModelAttribute("task") Task task,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("task",taskService.findTaskById(id));
            return "task-views/edit-task";
        }else {
            Task updatedTask = taskService.updateTask(task);

            Mechanic mechanic = getLoggedInMechanic();

            return "redirect:/api/mechanic/"+mechanic.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) throws DeleteTaskException {
        Task task = taskService.findTaskById(id);
        List<Appointment> appointments = new ArrayList<>();
        for (Mechanic mechanic : task.getMechanics()) {
            List<Appointment> pendingAndApprovedAppointmentsForEachMechanicWithThisTask = mechanic
                    .getAppointments()
                    .stream()
                    .filter(appointment -> appointment
                            .getAppointmentStatus()
                            .equals("APPROVED"))
                    .collect(Collectors.toList());

            appointments.addAll(pendingAndApprovedAppointmentsForEachMechanicWithThisTask);
        }

        if (! appointments.isEmpty()){
            throw new DeleteTaskException("Check pending and approved appointments");
        }else {
            logger.info("Task ==========> "+task);
            taskService.deleteTask(task);
            Mechanic mechanic = getLoggedInMechanic();
            return "redirect:/api/mechanic/"+mechanic.getId();
        }
    }

    private Mechanic getLoggedInMechanic() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser appUserByUserName = appUserService.findAppUserByUserName(name);

        return appUserByUserName.getMechanic();
    }

}
