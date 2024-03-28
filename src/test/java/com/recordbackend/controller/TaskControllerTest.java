package com.recordbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Model.Hierarchy;
import com.recordbackend.Model.Status;
import com.recordbackend.Service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @Value("${test.email}")
    private String email;

    @Value("${test.password}")
    private String password;

    @BeforeEach
    public void setUp() throws Exception {
        // Arrange
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andReturn();

        // Extract token from response
        String response = result.getResponse().getContentAsString();
        this.token = extractTokenFromResponse(response); // Implement this method to extract token from response
    }

    @Test
    public void testUpdateTask() throws Exception {
        // Arrange
        Long id = 602L;
        TaskDto taskDto = TaskDto.builder()
                .taskId(1L)
                .title("Task Title")
                .description("Task Description")
                .status(Status.ACTIVE)
                .hierarchy(Hierarchy.IMPORTANT)
                .listUserId(List.of(1L, 2L))
                .boardlistId(202L)
                .build();

        when(taskService.updateTask(any(Long.class), any(TaskDto.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/task/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token) // Add the Authorization header with the token
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Task Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Task Description")));
    }


    // Implement this method to extract token from response
    private String extractTokenFromResponse(String response) throws JsonProcessingException {
        Map<String, String> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, String>>(){});
        return responseMap.get("token");
    }
}
