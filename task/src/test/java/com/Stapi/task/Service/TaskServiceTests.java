package com.Stapi.task.Service;

import com.Stapi.task.Dto.TaskDto;
import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.TaskRepository;
import com.Stapi.task.services.TaskService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    public void init(){
        user = User.builder()
                .id(1)
                .username("Stapi")
                .password("Test")
                .build();
        task = Task.builder()
                .description("test")
                .user(user)
                .build();
        taskDto = TaskDto.builder()
                .description("test")
                .build();
    }

    @Test
    public void TaskService_CreateTask_ReturnsString(){

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskDto.setDeadline(LocalDate.now());
        taskDto.setStatus("pending");

        String string = taskService.createTask(taskDto, user);

        Assertions.assertThat(string).isInstanceOf(String.class);

        verify(taskRepository, times(1)).save(argThat(task1 -> task1.getDescription().equals(task.getDescription())));
    }

    @Test
    public void TaskService_DeleteTask_ReturnsVoid(){
        when(taskRepository.findByIdAndUser(task.getId(), user)).thenReturn(Optional.ofNullable(task));
        doNothing().when(taskRepository).delete(task);

        assertAll(()-> taskService.deleteTask(task.getId(), user));
        verify(taskRepository, times(1)).delete(task);

    }
}
