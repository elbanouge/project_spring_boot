package com.project.request_credit.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.request_credit.entities.Demande;

@Repository
public interface DemandeRepository extends CrudRepository<Demande, Long> {

}
