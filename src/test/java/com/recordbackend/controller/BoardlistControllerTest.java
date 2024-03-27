package com.recordbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.recordbackend.Dto.BoardlistDto;
import com.recordbackend.Service.BoardlistService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardlistService boardlistService;

    private ObjectMapper objectMapper = new ObjectMapper();
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
    public void testCreateNewBoardlistWithValidInput() throws Exception {
        // Arrange
        BoardlistDto boardlistDto = new BoardlistDto("Test Boardlist", 102L);
        when(boardlistService.createBoardlist(any(BoardlistDto.class))).thenReturn(boardlistDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/boardlist/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token) // Add the Authorization header with the token
                        .content(objectMapper.writeValueAsString(boardlistDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Boardlist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(102L));
    }

    // Implement this method to extract token from response
    private String extractTokenFromResponse(String response) throws JsonProcessingException {
        Map<String, String> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, String>>(){});
        return responseMap.get("token");
    }
}