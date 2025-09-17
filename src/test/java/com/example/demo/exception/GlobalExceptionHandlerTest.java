package com.example.demo.exception;

import com.example.demo.controller.TasksController;
import com.example.demo.service.TasksService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TasksController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TasksService tasksService;

    @Test
    void itShouldHandleResourceNotFound() throws Exception {
        int taskId = 999;

        when(tasksService.getTaskById(taskId)).thenThrow(new ResourceNotFoundException("Task not found with id " + taskId));

        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(jsonPath("$.error", CoreMatchers.is("Not Found")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Task not found with id " + taskId)));
    }

    @Test
    void itShouldHandleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/non-existent-url"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(jsonPath("$.error", CoreMatchers.is("Not Found")))
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is("The requested URL was not found on this server")));
    }

    @Test
    void itShouldHandleValidationException() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.error", CoreMatchers.is("Validation failed")))
                .andExpect(jsonPath("$.message",
                        CoreMatchers.containsString("description must not be blank")));
    }
}
