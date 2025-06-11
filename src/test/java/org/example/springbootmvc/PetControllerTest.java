package org.example.springbootmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootmvc.exception.EntityNotFoundException;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.model.User;
import org.example.springbootmvc.service.PetService;
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
public class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSuccessCreatePet() throws Exception {
        User user = new User("Виталий", "vitaly@mail.ru", 25);
        User createdUser = userService.createUser(user);

        Pet pet = new Pet("bob", createdUser.getId());

        String newPet = objectMapper.writeValueAsString(pet);

        String jsonResponse = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPet))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Pet petResponse = objectMapper.readValue(jsonResponse, Pet.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(pet.getName(), petResponse.getName());
        Assertions.assertEquals(pet.getUserId(), petResponse.getUserId());

        Assertions.assertDoesNotThrow(() -> petService.getPetById(petResponse.getId()));

        User userWithPet = userService.getUserById(user.getId());
        Assertions.assertEquals(1, userWithPet.getPets().size());
        Assertions.assertEquals(petResponse.getId(), userWithPet.getPets().get(0).getId());
    }

    @Test
    void shouldSuccessDeletePet() throws Exception {
        User user = new User("Виталий", "vitaly@mail.ru", 25);
        User createdUser = userService.createUser(user);

        Pet pet = new Pet("bob", createdUser.getId());
        Pet createdPet = petService.createPet(pet);

        mockMvc.perform(delete("/pets/{id}", createdPet.getId()))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> petService.getPetById(createdPet.getId()));
    }

    @Test
    void shouldSuccessSearchPetById() throws Exception {
        User user = new User("Виталий", "vitaly@mail.ru", 25);
        User createdUser = userService.createUser(user);

        Pet pet = new Pet("bob", createdUser.getId());
        Pet createdPet = petService.createPet(pet);

        String foundPetJson = mockMvc.perform(get("/pets/{id}", createdPet.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Pet foundPet = objectMapper.readValue(foundPetJson, Pet.class);

        assertThat(pet)
                .usingRecursiveComparison()
                .isEqualTo(foundPet);
    }
}
