package org.example.springbootmvc.model;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class User {

    @Null(message = "не должно отправляться в теле запросе")
    private Long id;

    @Size(max = 50)
    @NotBlank
    private String name;
    @Email
    private String email;

    @Min(0)
    @NotNull
    private Integer age;

    private List<Pet> pets = new ArrayList<>();

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", pets=" + pets +
                '}';
    }
}
