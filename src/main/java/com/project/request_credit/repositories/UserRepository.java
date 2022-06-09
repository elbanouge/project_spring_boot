package com.project.request_credit.repositories;

import java.util.List;
import java.util.Optional;

import com.project.request_credit.entities.User;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll(Sort sort);
}
