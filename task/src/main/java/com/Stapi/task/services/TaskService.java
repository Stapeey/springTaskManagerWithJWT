package com.Stapi.task.services;

import com.Stapi.task.Dto.TaskDto;
import com.Stapi.task.Exceptions.MissingFieldsException;
import com.Stapi.task.Exceptions.TaskNotFoundException;
import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> listTasks(User user){
        List<Task> tasks = taskRepository.findByUser(user);
        return tasks;
    }

    public String createTask(TaskDto taskDto, User user){
        Task task = new Task();
        try {
            task.setDeadline(taskDto.getDeadline());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());
            task.setUser(user);
        }
        catch (Exception ex){
            throw new MissingFieldsException("Incomplete task sent in. try this format: deadline, description, status");
        }

        Task saveTask = taskRepository.save(task);

        return "Task successfully created!";
    }

    public String updateTask(Task task, User user){
        Task taskToModify = taskRepository.findByIdAndUser(task.getId(), user).orElseThrow(() -> new TaskNotFoundException("Task with that id does not exist"));
        if(task.getDeadline() != null){
            taskToModify.setDeadline(task.getDeadline());
        }
        if(task.getDescription() != null){
            taskToModify.setDescription(task.getDescription());
        }
        if(task.getStatus() != null){
            taskToModify.setStatus(task.getStatus());
        }

        taskRepository.save(taskToModify);
        return "Task updated successfully";
    }

    public void deleteTask(Integer id, User user){
        Task toDelete = taskRepository.findByIdAndUser(id, user).orElseThrow(() -> new TaskNotFoundException("Task with that id does not exist"));
        taskRepository.delete(toDelete);
    }
}
