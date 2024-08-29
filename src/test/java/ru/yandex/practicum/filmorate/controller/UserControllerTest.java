package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void returnUsersListWhenGet() throws Exception {
        final User user1 = new User();
        user1.setId(1L);

        final User user2 = new User();
        user2.setId(2L);

        final List<User> users = Arrays.asList(user1, user2);
        when(userService.all()).thenReturn(users);
        String response = this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(users), response);
    }

    @Test
    void postAndPutResponseStatusBadRequestWhenValidationFails() throws Exception {
        //email do not match pattern
        final User user1 = new User();
        user1.setEmail("aaaaa.aa");
        user1.setLogin("aaa");
        user1.setBirthday(LocalDate.of(2023,1,1));

        perfomPostObjectBadRequest(user1);
        perfomPutObjectBadRequest(user1);

        //whitespace in login
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aa a");
        user1.setBirthday(LocalDate.of(2023,1,1));

        perfomPostObjectBadRequest(user1);
        perfomPutObjectBadRequest(user1);

        //birthday in future
        user1.setEmail("aa@aaa.aa");
        user1.setLogin("aaa");
        user1.setBirthday(LocalDate.of(2025,1,1));

        perfomPostObjectBadRequest(user1);
        perfomPutObjectBadRequest(user1);

    }

    private void perfomPostObjectBadRequest(User user) throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).save(user);
    }

    private void perfomPutObjectBadRequest(User user) throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(user);
    }
}