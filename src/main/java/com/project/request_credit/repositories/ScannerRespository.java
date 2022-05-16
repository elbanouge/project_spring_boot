package com.project.request_credit.repositories;

import com.project.request_credit.entities.Scanner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerRespository extends JpaRepository<Scanner, Long> {
    Scanner findByUrl(String url);
}
