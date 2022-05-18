package com.example.creditproject.repositories;


import com.example.creditproject.entities.Credit;
import com.example.creditproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findByuser(User iduser);
    Credit findByUser(User user);
}
