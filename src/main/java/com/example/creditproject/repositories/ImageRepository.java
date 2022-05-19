package com.example.creditproject.repositories;

import java.util.Optional;

import com.example.creditproject.models.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByName(String name);
}
