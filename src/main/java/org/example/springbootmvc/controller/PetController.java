package org.example.springbootmvc.controller;

import jakarta.validation.Valid;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping()
    public ResponseEntity<Pet> createPet(@RequestBody @Valid Pet pet) {
        log.info("Creating pet: {}", pet);
        Pet createdPet = petService.createPet(pet);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPet);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@RequestBody @Valid Pet pet, @PathVariable("id") long id) {
        log.info("Updating pet: {}", pet);
        return petService.updatePet(pet, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") long id) {
        petService.deletePet(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable("id") long id) {
        log.info("Getting pet by id: {}", id);
        return petService.getPetById(id);
    }
}
