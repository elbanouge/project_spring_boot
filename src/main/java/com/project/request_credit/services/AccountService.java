package com.project.request_credit.services;

import java.util.List;

import com.project.request_credit.entities.Role;
import com.project.request_credit.entities.User;

public interface AccountService {

    public Role createNewRole(Role role);

    public User createNewUser(User user);

    public boolean updateUser(User user);

    public boolean addRoleToUser(String username, String name);

    public List<User> getAllUsers();

    public User findUserByEmail(String email);

    public User findUserByUsername(String username);

    public Role findRoleByName(String name);

    public User updatePassword(String password, String email);

    public User findById(Long id);

}
