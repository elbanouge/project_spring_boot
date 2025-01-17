package com.project.request_credit.controllers;

import com.project.request_credit.models.Credit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/credit")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class SimulationController {

    @PostMapping({ "calculMensualite" })
    public ResponseEntity<?> calculMensualite(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double a = Math.pow(1 + tm, n) * tm * c / (Math.pow(1 + tm, n) - 1);
        credit.setMensualite(a);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping({ "calculCapital" })
    public ResponseEntity<?> calculCapital(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double a = credit.getMensualite();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double c = (a * (Math.pow(1 + tm, n) - 1)) / (Math.pow(1 + tm, n) * tm);
        credit.setCapital(c);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping({ "calculDuree" })
    public ResponseEntity<?> calculDuree(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        double a = credit.getMensualite();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double n = Math.log(a / (a - (tm * c))) / Math.log(1 + tm);
        credit.setDuree((int) n);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }
}