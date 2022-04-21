package com.example.creditproject.services;

import java.util.List;

import com.example.creditproject.models.User;

public interface UserService {
    User save(User user);

    User findByEmail(String email);

    List<User> findAllUsers();

}
