package com.example.creditproject.repositories;

import java.util.Optional;

import com.example.creditproject.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
