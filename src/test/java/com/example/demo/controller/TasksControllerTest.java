package com.example.demo.controller;

import com.example.demo.dtos.TaskDTO;
import com.example.demo.models.Task;
import com.example.demo.service.TasksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = TasksController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TasksService tasksService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    public void setup() {
        task = Task.builder().id(1).description("Task Description").completed(false).build();
        taskDTO = TaskDTO.builder().description("Task Description").completed(false).build();
    }

    @Test
    void itShouldGetTasks() throws Exception {
        List<Task> response = List.of(task);
        when(tasksService.getTasks()).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/tasks"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(response.size())))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(task.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", CoreMatchers.is(task.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].completed", CoreMatchers.is(task.isCompleted())));
    }

    @Test
    void itShouldGetTaskById() throws Exception {
        int taskId = 1;
        when(tasksService.getTaskById(taskId)).thenReturn(task);

        ResultActions result = mockMvc.perform(get("/tasks/" + taskId));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(task.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(task.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed", CoreMatchers.is(task.isCompleted())));
    }

    @Test
    void itShouldCreateTask() throws Exception {
        given(tasksService.createTask(any(TaskDTO.class))).willAnswer(invocation -> {
            TaskDTO dto = invocation.getArgument(0);
            return Task.builder()
                    .id(1)
                    .description(dto.getDescription())
                    .completed(dto.isCompleted())
                    .build();
        });

        ResultActions result = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
              .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO.getDescription())))
              .andExpect(MockMvcResultMatchers.jsonPath("$.completed", CoreMatchers.is(task.isCompleted())));
    }

    @Test
    void itShouldUpdateTask() throws Exception {
        int taskId = 1;
        when(tasksService.updateTask(taskId, taskDTO)).thenReturn(taskDTO);

        ResultActions result = mockMvc.perform(put("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDTO.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed", CoreMatchers.is(task.isCompleted())));
    }

    @Test
    void itShouldDeleteTask() throws Exception {
        int taskId = 1;
        doNothing().when(tasksService).deleteTask(taskId);

        ResultActions result = mockMvc.perform(delete("/tasks/" + taskId));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}