package com.Stapi.task.Security;

import com.Stapi.task.repositories.UserRepository;
import com.Stapi.task.security.*;
import com.Stapi.task.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityFilterChainTests {
    @Autowired
    private MockMvc mockMvc;

    /*@MockBean
    private JWTAuthenticator jwtAuthenticator;

    @MockBean
    private AuthEntryPoint authEntryPoint;

    @MockBean
    private GenerateJWT generateJWT;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TaskService taskService;*/


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void SecurityFilterChain_Unauthenticated_ReturnsUnauthorized() throws Exception {

        ResultActions response = mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void SecurityFilterChain_HttpBasic_ReturnsForbiddenOrUnauthorized() throws Exception{
        String username = "Stapi";
        String password = "Test";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        ResultActions response = mockMvc.perform(get("/listall")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Stapi")
    public void SecurityFilterChain_LoggedIn_ReturnsCorrectly() throws Exception{

        ResultActions response = mockMvc.perform(get("/listall")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(notNullValue()));
    }


}
