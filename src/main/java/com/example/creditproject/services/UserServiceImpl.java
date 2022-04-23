package com.example.creditproject.services;

import java.util.List;

import com.example.creditproject.models.User;
import com.example.creditproject.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User updatePassworde(String password,String email) {
        User u=userRepository.findByEmail(email).orElse(null);
        u.setPassword(password);

        return userRepository.save(u);
    }

    @Override
    public User updatePassword(String email) {
        User u=userRepository.findByEmail(email).orElse(null);
        //u.setPassword(password);

        return u;
    }
}
