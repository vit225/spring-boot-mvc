package org.example.springbootmvc.service;

import org.example.springbootmvc.exception.EntityNotFoundException;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final Map<Long, Pet> pets = new HashMap<>();
    private final UserService userService;

    private long counterId;

    public PetService(UserService userService) {
        this.userService = userService;
    }

    public Pet createPet(Pet pet) {
        if (pet.getUserId() == null || userService.getUserById(pet.getUserId()) == null) {
            throw new EntityNotFoundException("User not found with id " + pet.getUserId());
        }
        pet.setId(++counterId);
        pets.put(pet.getId(), pet);

        userService.getUsers().get(pet.getUserId())
                .getPets().add(pet);
        return pet;
    }

    public Pet updatePet(Pet pet, long id) {
        Pet findPet = userService.getUsers().get(pet.getUserId())
                .getPets()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (findPet != null) {
            findPet.setName(pet.getName());
            findPet.setUserId(pet.getUserId());
            pets.put(findPet.getId(), findPet);
        }
        return findPet;
    }

    public void deletePet(long id) {
        Pet findPet = pets.get(id);

        if (findPet == null) {
            throw new EntityNotFoundException("Pet not found with id " + id);
        }

        Long userId = findPet.getUserId();

        pets.remove(id);

        User user = userService.getUserById(userId);
        if (user != null && user.getPets() != null) {
            user.setPets(user.getPets().stream()
                    .filter(pet -> !pet.getId().equals(id))
                    .collect(Collectors.toList()));
        }
    }

    public Pet getPetById(long id) {
        Pet findPet = pets.get(id);

        if (findPet == null) {
            throw new EntityNotFoundException("Pet not found with id " + id);
        }

        return findPet;
    }
}
