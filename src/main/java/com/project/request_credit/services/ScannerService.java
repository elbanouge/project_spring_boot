package com.project.request_credit.services;

import java.util.List;

import com.project.request_credit.entities.Scanner;
import com.project.request_credit.repositories.ScannerRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScannerService {

    @Autowired
    private ScannerRespository scannerRepository;

    public Scanner getScannerByUrl(String url) {
        return scannerRepository.findByUrl(url);
    }

    public Scanner saveScanner(Scanner scanner) {
        return scannerRepository.save(scanner);
    }

    public List<Scanner> getScanners() {
        return scannerRepository.findAll();
    }

    public void delete(Scanner image) {
        scannerRepository.delete(image);
    }

    public Scanner findById(Long id) {
        return scannerRepository.findById(id).orElse(null);
    }
}
