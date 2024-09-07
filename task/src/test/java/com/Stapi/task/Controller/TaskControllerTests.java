package com.Stapi.task.Controller;

import com.Stapi.task.Controllers.TaskController;
import com.Stapi.task.Dto.TaskDto;
import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.UserRepository;
import com.Stapi.task.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "Stapi")
public class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;
    private Task task;
    private User user;
    private TaskDto taskDto;

    @BeforeEach
    public void init(){
        task = Task.builder()
                .description("a")
                .id(1)
                .deadline(LocalDate.now())
                .status("pending")
                .build();
        taskDto = TaskDto.builder()
                .description("a")
                .deadline(LocalDate.now())
                .status("pending")
                .build();
        user = User.builder()
                .username("Stapi")
                .password("test")
                .id(1)
                .build();
        when(userRepository.findByUsername("Stapi")).thenReturn(java.util.Optional.of(user));
    }

    @Test
    //@WithMockUser(username = "Stapi")
    public void TaskController_ListAll_ReturnsListOfTasksOrEmpty() throws Exception {
        when(taskService.listTasks(ArgumentMatchers.any(User.class))).thenReturn(Arrays.asList(task));



        ResultActions response = mockMvc.perform(get("/listall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(task.getId())));
    }

    @Test
    //@WithMockUser(username = "Stapi")
    public void TaskController_Update_ReturnsString() throws Exception {
        when(taskService.updateTask(task,user)).thenReturn("Updated");

        ResultActions response = mockMvc.perform(put("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andExpect(MockMvcResultMatchers.status().isOk());

        verify(taskService).updateTask(task, user);
    }

    @Test
    //@WithMockUser(username = "Stapi")
    public void TaskController_Delete_ReturnsString() throws Exception {
        doNothing().when(taskService).deleteTask(task.getId(),user);

        ResultActions response = mockMvc.perform(delete("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Deleted task successfully"));

        verify(taskService).deleteTask(task.getId(), user);
    }

}
