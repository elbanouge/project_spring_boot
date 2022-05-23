package com.project.request_credit.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;
import com.project.request_credit.services.AccountService;
import com.project.request_credit.services.CreditService;

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

@RequestMapping("/api/credit")
@RestController
public class CreditController {
    @Autowired
    CreditService creditService;

    @Autowired
    AccountService accountService;

    @GetMapping("all")
    public List<Credit> getAllContacts() {
        return creditService.getAllCredit();
    }

    @GetMapping(value = "all/{id}")
    public Credit getById(@PathVariable int id) {
        return creditService.getCreditById(id);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Credit credit = creditService.getCreditById(id);
        creditService.deleteCredit(id);
        return new ResponseEntity<>(credit, HttpStatus.OK);
    }

    @PostMapping(value = "add/{username}")
    public Credit add(
            @RequestBody Credit credit, @PathVariable String username) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("'demander le 'dd/MM/yyyy 'a' hh:mm");
        String format = formatter.format(date);
        System.out.println(format);
        credit.setDate(format);
        User user = accountService.findUserByUsername(username);
        return creditService.addCredit(credit, user);
    }

    @PutMapping(value = "update/{id}")
    public Credit update(
            @RequestBody Credit credit, @PathVariable long id) {
        Credit credit1 = creditService.getCreditById(id);
        credit1.setCapital(credit.getCapital());
        credit1.setDuree(credit.getDuree());
        credit1.setMensualite(credit.getMensualite());
        Date date = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("'demander le 'dd/MM/yyyy 'a' hh:mm");
        String format = formatter.format(date);
        System.out.println(format);
        credit1.setDate(format);
        return creditService.updateCredit(credit1, id);
    }

    @GetMapping(value = "allcredit/{email}")
    public List<Credit> getByemail(@PathVariable String email) {
        User u = accountService.findUserByEmail(email);
        return creditService.CreditByidUser(u);
    }

    @GetMapping(value = "credit/{email}")
    public Credit getByOneemail(@PathVariable String email) {
        User u = accountService.findUserByEmail(email);

        return creditService.OneCreditByidUser(u);
    }

    @GetMapping(value = "getnombreCredit/{email}")
    public int getnombreCredit(@PathVariable String email) {
        User u = accountService.findUserByEmail(email);
        List<Credit> aa = creditService.CreditByidUser(u);
        return aa.size();
    }
}
