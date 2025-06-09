package org.example.springbootmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootmvc.exception.EntityNotFoundException;
import org.example.springbootmvc.model.User;
import org.example.springbootmvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUser() throws Exception {
        User user = new User("Виталий", "email@mail.ru", 20);

        String userJson = objectMapper.writeValueAsString(user);

        String createUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        User userResponse = objectMapper.readValue(createUserJson, User.class);

        Assertions.assertEquals(0, userResponse.getPets().size());
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(user.getName(), userResponse.getName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getAge(), userResponse.getAge());
    }

    @Test
    void shouldSuccessSearchUserById() throws Exception {
        User user = new User("Виталий", "email@mail.ru", 20);

        user = userService.createUser(user);

        String foundUserJson = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User foundUser = objectMapper.readValue(foundUserJson, User.class);

        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldSuccessDeleteUser() throws Exception {
        User user = new User("Виталий", "vitaly@mail.ru", 25);
        User createdUser = userService.createUser(user);

        mockMvc.perform(delete("/users/{id}", createdUser.getId()))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(createdUser.getId()));
    }
}
