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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardlistService boardlistService;

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
    public void testCreateNewBoardlist() throws Exception {
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

    @Test
    public void testGetAllBoardlist() throws Exception {
        // Arrange
        List<BoardlistDto> boardlistDtos = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BoardlistDto boardlistDto = BoardlistDto.builder()
                    .name("Test Boardlist " + i)
                    .projectId(102L)
                    .build();
            boardlistDtos.add(boardlistDto);
        }

        when(boardlistService.getAllBoardlist()).thenReturn(boardlistDtos);

        // Act

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/boardlist/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)) // Add the Authorization header with the token
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(20)));
    }

    // Implement this method to extract token from response
    private String extractTokenFromResponse(String response) throws JsonProcessingException {
        Map<String, String> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, String>>(){});
        return responseMap.get("token");
    }
}