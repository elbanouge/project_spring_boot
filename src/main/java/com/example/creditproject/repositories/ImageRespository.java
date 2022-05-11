package com.example.creditproject.repositories;

import com.example.creditproject.entities.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRespository extends JpaRepository<Image, Long> {
    Image findByUrl(String url);
}
