package com.project.request_credit.services;

import java.util.List;

import com.project.request_credit.entities.Scanner;
import com.project.request_credit.entities.User;
import com.project.request_credit.repositories.ScannerRespository;
import com.project.request_credit.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScannerService {

    @Autowired
    private ScannerRespository scannerRepository;

    @Autowired
    private UserRepository userRepository;

    public Scanner getScannerByUrl(String url) {
        return scannerRepository.findByUrl(url);
    }

    public Scanner saveScanner(Scanner scanner) {
        return scannerRepository.save(scanner);
    }

    public List<Scanner> getScanners() {
        return scannerRepository.findAll();
    }

    public void deleteById(Long id) {
        Scanner image = scannerRepository.findById(id).orElse(null);
        List<User> users = userRepository.findAll();
        for (User user : users) {
            for (Scanner scanner : user.getScanners()) {
                if (scanner.getId().equals(image.getId())) {
                    user.getScanners().remove(scanner);
                    userRepository.save(user);
                }
            }
        }
        scannerRepository.delete(image);
    }

    public Scanner findById(Long id) {
        return scannerRepository.findById(id).orElse(null);
    }
}
