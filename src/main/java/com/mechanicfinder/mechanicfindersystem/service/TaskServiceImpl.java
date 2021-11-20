package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.exception.TaskWithTheProvidedNameExists;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import com.mechanicfinder.mechanicfindersystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final MechanicRepository mechanicRepository;

    @Override
    public Task findTaskByTaskName(String taskName) {
        return taskRepository.findTaskByTaskName(taskName);
    }

    @Override
    @Transactional
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task addTask(Long mechanicId, Task task) throws TaskWithTheProvidedNameExists {
        Mechanic mechanic = mechanicRepository.findMechanicById(mechanicId);
        Task addedTask = taskRepository.findTaskByTaskName(task.getTaskName());
        if (task != addedTask){
            mechanic.getTasks().add(task);
            mechanicRepository.save(mechanic);
        }else{
            throw new TaskWithTheProvidedNameExists("Task "+task.getTaskName()+" is present");
        }
        return addedTask;
    }

}
