package com.project.request_credit.controllers;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.project.request_credit.models.CreditModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/simulation")
@RestController
public class SimulationController {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @PostMapping({ "calculMensualite" })
    public ResponseEntity<?> calculMensualite(@RequestBody CreditModel credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double a = Math.pow(1 + tm, n) * tm * c / (Math.pow(1 + tm, n) - 1);
        double af = Double.parseDouble(df.format(a).replace(",", "."));
        credit.setMensualite(af);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping({ "calculCapital" })
    public ResponseEntity<?> calculCapital(@RequestBody CreditModel credit) {
        double t = credit.getTaux() / 100;
        double a = credit.getMensualite();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double c = (a * (Math.pow(1 + tm, n) - 1)) / (Math.pow(1 + tm, n) * tm);
        df.setRoundingMode(RoundingMode.UP);
        double cf = Double.parseDouble(df.format(c).split(",")[0]);
        credit.setCapital(cf);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping({ "calculDuree" })
    public ResponseEntity<?> calculDuree(@RequestBody CreditModel credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        double a = credit.getMensualite();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double n = Math.log(a / (a - (tm * c))) / Math.log(1 + tm);
        credit.setDuree((int) n);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }
}