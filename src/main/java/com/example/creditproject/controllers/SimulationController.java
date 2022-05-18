package com.example.creditproject.controllers;

import com.example.creditproject.entities.User;
import com.example.creditproject.models.Credit;

import com.example.creditproject.services.CreditServices;
import com.example.creditproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/credit")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class SimulationController {
    @Autowired
    UserService userService;
    @Autowired
    CreditServices creditServices;
    @PostMapping("/calculAnnuite")
    public ResponseEntity<?> calculAnnuite(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double a = Math.pow(1 + tm, n) * tm * c / (Math.pow(1 + tm, n) - 1);
        credit.setAnnuite(a);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping("/calculCapital")
    public ResponseEntity<?> calculCapital(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double a = credit.getAnnuite();
        int n = credit.getDuree();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double c = (a * (Math.pow(1 + tm, n) - 1)) / (Math.pow(1 + tm, n) * tm);
        credit.setCapital(c);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @PostMapping("/calculDuree")
    public ResponseEntity<?> calculDuree(@RequestBody Credit credit) {
        double t = credit.getTaux() / 100;
        double c = credit.getCapital();
        double a = credit.getAnnuite();

        double tm = Math.pow(1 + t, (double) 1 / 12) - 1;
        double n = Math.log(a / (a - (tm * c))) / Math.log(1 + tm);
        credit.setDuree((int) n);

        return new ResponseEntity<>(credit, HttpStatus.ACCEPTED);
    }

    @GetMapping("all")
    public List<com.example.creditproject.entities.Credit> getAllContacts()
    {

        return creditServices.getAllCredit();
    }

    @GetMapping(value="all/{id}")
    public com.example.creditproject.entities.Credit getById(@PathVariable int id)
    {

        return creditServices.getCreditById(id);
    }

    @DeleteMapping(value="delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id)
    {
        com.example.creditproject.entities.Credit credit=creditServices.getCreditById(id);
        //User user=userService.findById(credit.getUser().getId());
        creditServices.deleteCredit(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "add")
    public com.example.creditproject.entities.Credit add(@RequestBody com.example.creditproject.entities.Credit credit)
    {
        Date date = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("'demander le 'dd/MM/yyyy 'a' hh:mm");
        String format = formatter.format(date);
        System.out.println(format);
        credit.setCreditdate(format);
        credit.getUser().getId();
        User user=userService.findByEmail(credit.getUser().getEmail());
        credit.setUser(user);
        return creditServices.addCredit(credit);
        //return null;
    }

    @PutMapping(value = "update/{id}")
    public com.example.creditproject.entities.Credit update(@RequestBody com.example.creditproject.entities.Credit credit, @PathVariable long id) {
        com.example.creditproject.entities.Credit credit1=creditServices.getCreditById(id);
        //User user=userService.findById(credit.getUser().getId());
        credit1.setCapital(credit.getCapital());
        credit1.setDuree(credit.getDuree());
        credit1.setMensualite(credit.getMensualite());
        Date date = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("'demander le 'dd/MM/yyyy 'a' hh:mm");
        String format = formatter.format(date);
        System.out.println(format);
        credit1.setCreditdate(format);
        return creditServices.updateCredit(credit1,id);
    }


    @GetMapping(value="allcredit/{email}")
    public List<com.example.creditproject.entities.Credit> getByemail(@PathVariable String email)
    {
        User u=userService.findByEmail(email);

        return creditServices.CreditByidUser(u);
    }

    @GetMapping(value="credit/{email}")
    public com.example.creditproject.entities.Credit getByOneemail(@PathVariable String email)
    {
        User u=userService.findByEmail(email);

        return creditServices.OneCreditByidUser(u);
    }


    @GetMapping(value="getnombreCredit/{email}")
    public int getnombreCredit(@PathVariable String email)
    {
        User u=userService.findByEmail(email);
        List<com.example.creditproject.entities.Credit> aa=creditServices.CreditByidUser(u);
        int count=0;
        for(com.example.creditproject.entities.Credit tt:aa) count++;
        return count;
    }
}