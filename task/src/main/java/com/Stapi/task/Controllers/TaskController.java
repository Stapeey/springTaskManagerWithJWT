package com.Stapi.task.Controllers;

import com.Stapi.task.Dto.TaskDto;
import com.Stapi.task.Exceptions.MissingFieldsException;
import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.UserRepository;
import com.Stapi.task.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private TaskService taskService;
    private UserRepository userRepository;

    @Autowired TaskController(TaskService taskService, UserRepository userRepository){
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow(()->new UsernameNotFoundException("Username not found"));
        return user;
    }

    @GetMapping("/listall")
    private ResponseEntity<List<Task>> ListAll(){
        List<Task> response = taskService.listTasks(getUser());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    private ResponseEntity<String> Create(@RequestBody TaskDto taskDto){
        if (taskDto == null || taskDto.getDeadline() == null || taskDto.getDescription() == null || taskDto.getStatus() == null){
            throw new MissingFieldsException("provide correct details about the task");
        }

        String response = taskService.createTask(taskDto, getUser());

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/update")
    private ResponseEntity<String> Update(@RequestBody Task task){
        String response = taskService.updateTask(task, getUser());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<String> Delete(@RequestBody Task id){
        taskService.deleteTask(id.getId(), getUser());
        return new ResponseEntity<>("Deleted task successfully", HttpStatus.OK);
    }
}
