package com.example.creditproject.services;

import java.util.List;

import com.example.creditproject.entities.User;

public interface UserService {
    User save(User user);

    User findByEmail(String email);

    List<User> findAllUsers();

    User updatePassword(String password,String email);

    public User updatePassword(String email);

    public void delete(Long id);
    public void deleteAll();

    User findById(Long userId);

    public void deleteByEmail(String email);

}
