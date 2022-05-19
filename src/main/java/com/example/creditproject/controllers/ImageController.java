package com.example.creditproject.controllers;
import java.io.IOException;
import java.util.Optional;

import com.example.creditproject.models.Image;
import com.example.creditproject.models.ImageUploadResponse;
import com.example.creditproject.repositories.ImageRepository;
import com.example.creditproject.services.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/api/image")
@RestController
//@CrossOrigin(origins = "http://localhost:8082") open for specific port
@CrossOrigin() // open for all ports
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    //, consumes = MediaType.IMAGE_JPEG_VALUE
    @PostMapping(value="/upload" )
    public ResponseEntity<ImageUploadResponse> uplaodImage(@RequestPart(value = "file",required = false) MultipartFile file)
            throws IOException {
        Image img=new Image();
        img.setName(file.getOriginalFilename());
        img.setType(file.getContentType());
        img.setImage(ImageService.compressImage(file.getBytes()));
        imageRepository.save(img);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }

    @GetMapping(path = {"/get/info/{name}"})
    public Image getImageDetails(@PathVariable("name") String name) throws IOException {

        final Optional<Image> dbImage = imageRepository.findByName(name);

        return dbImage.orElse(null);
    }

    @GetMapping(path = {"/get/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<Image> dbImage = imageRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageService.decompressImage(dbImage.get().getImage()));
    }
}
