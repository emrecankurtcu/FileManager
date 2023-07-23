package com.etstur.filemanager;

import com.etstur.filemanager.dto.request.LoginRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("test@test.test").password("12").build();

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testLoginValidCredentials() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("test@test.test").password("1").build();
        String firstName = "Test";
        String lastName = "User";

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andDo(print());
    }
}
