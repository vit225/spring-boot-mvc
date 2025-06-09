package org.example.springbootmvc.service;

import org.example.springbootmvc.exception.EntityNotFoundException;
import org.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private long countId;

    public User createUser(User user) {
        user.setId(++countId);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        user.setId(id);
        if (user.getPets() == null || user.getPets().isEmpty()) {
            user.setPets(users.get(id).getPets());
        }
        users.put(id, user);
        return user;
    }

    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        users.remove(id);
    }

    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
