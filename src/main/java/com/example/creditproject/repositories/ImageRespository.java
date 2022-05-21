package com.example.creditproject.repositories;

import com.example.creditproject.entities.Imagetest;

import com.example.creditproject.entities.Imagetest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRespository extends JpaRepository<Imagetest, Long> {
    Imagetest findByUrl(String url);
}
