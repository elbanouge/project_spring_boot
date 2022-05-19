package com.example.creditproject.controllers;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import com.example.creditproject.entities.User;
import com.example.creditproject.models.Email;
import com.example.creditproject.services.EmailSenderService;
import com.example.creditproject.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/email")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class EmailController {

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    UserService userService;

    // @PostMapping("sendOTP")
    // public ResponseEntity<?> sendEmailAvecOTP(@RequestBody Email email) {
    //     Random rand = new Random();
    //     int otp = rand.nextInt(999999);

    //     email.setMessage(email.getMessage() + " " + otp);
    //     boolean bool = emailSenderService.sendEmail(email.getEmail(), email.getSubject(), email.getMessage());
    //     User user = userService.findByEmail(email.getEmail());

    //     if (bool == true && user != null) {
    //         System.out.println(user.toString());
    //         user.setOtp(otp);
    //         user.setOtpExpiry(new Date());
    //         userService.save(user);

    //         return ResponseEntity.ok("Email sent successfully");
    //     } else
    //         return ResponseEntity.badRequest().body("Email not sent");
    // }
    private int otp;

    @PostMapping("sendOTP")
    public ResponseEntity<String> sendEmailAvecOTP(@RequestBody Email email) {
        Random rand = new Random();
        int otp = rand.nextInt(999999);

        email.setMessage(email.getMessage() + " " + otp);
        boolean bool = emailSenderService.sendEmail(email.getEmail(), email.getSubject(), email.getMessage());
        if (bool)
        {
            this.otp=otp;
            return ResponseEntity.ok("envoyé");
        }
        else
            return ResponseEntity.badRequest().body("non envoyé");
    }

    @PostMapping("sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody Email email) {
        boolean bool = emailSenderService.sendEmail(email.getEmail(), email.getSubject(), email.getMessage());
        if (bool)
            return ResponseEntity.ok("Email sent successfully");
        else
            return ResponseEntity.badRequest().body("Email not sent");
    }

    @PostMapping("verifyOTP")
    public ResponseEntity<String> verifyOTP(@RequestBody int receivedOtp) {
       
        if (receivedOtp==otp)
        {
            return ResponseEntity.ok("match");
        }
        else
            return  ResponseEntity.badRequest().body("no match");
    }
    @GetMapping("verifyOTP/{email}/{otp}")
    public ResponseEntity<?> verifierOTP(@PathVariable String email, @PathVariable int otp) {
        User user = userService.findByEmail(email);
        if (user != null) {
            if (user.getOtp() == otp) {
                if (user.getOtpExpiry().toInstant().isAfter(new Date().toInstant().minus(5, ChronoUnit.MINUTES))) {
                    user.setOtp(0);
                    user.setOtpExpiry(null);
                    userService.save(user);
                    return ResponseEntity.ok("OTP verified successfully");
                } else
                    return ResponseEntity.badRequest().body("OTP expired");
            } else
                return ResponseEntity.badRequest().body("OTP not verified");
        } else
            return ResponseEntity.badRequest().body("User not found");
    }
}
