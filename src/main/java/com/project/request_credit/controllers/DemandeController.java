package com.project.request_credit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.Demande;
import com.project.request_credit.services.CreditService;
import com.project.request_credit.services.DemandeService;

@RequestMapping("/api/demande")
@RestController
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private CreditService creditService;

    @PostMapping({ "addDemande{id}" })
    public ResponseEntity<?> add(@RequestBody Demande demande, @PathVariable long id) {
        Credit credit = creditService.getCreditById(id);
        demandeService.addDemande(demande, credit);
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }

    @PutMapping({ "updateDemande{id}" })
    public ResponseEntity<?> update(@RequestBody Demande demande, @PathVariable long id) {
        demandeService.updateDemande(demande, id);
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }

    @DeleteMapping({ "deleteDemande{id}" })
    public ResponseEntity<?> delete(@PathVariable long id) {
        demandeService.deleteDemande(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping({ "getDemande{id}" })
    public ResponseEntity<?> get(@PathVariable long id) {
        Demande demande = demandeService.getDemandeById(id);
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }

    @GetMapping({ "getAllDemande" })
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(demandeService.getAllDemande(), HttpStatus.OK);
    }

}
