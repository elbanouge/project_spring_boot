package com.project.request_credit.repositories;

import java.util.List;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Long> {

    List<Credit> findCreditsByUser(User user);

    Credit findByUser(User user);
}
