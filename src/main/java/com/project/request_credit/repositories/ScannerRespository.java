package com.project.request_credit.repositories;

import com.project.request_credit.entities.Scanner;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerRespository extends CrudRepository<Scanner, Long> {
    Scanner findByUrl(String url);
}
