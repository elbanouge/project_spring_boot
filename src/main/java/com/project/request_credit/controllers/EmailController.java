package com.project.request_credit.controllers;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import com.project.request_credit.entities.User;
import com.project.request_credit.models.Email;
import com.project.request_credit.services.AccountService;
import com.project.request_credit.services.EmailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/email")
@RestController
public class EmailController {

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    AccountService userService;

    @PostMapping({ "sendOTP" })
    public ResponseEntity<?> sendEmailAvecOTP(@RequestBody Email email) {
        Random rand = new Random();
        int otp = rand.nextInt(999999);

        email.setMessage(email.getMessage() + " " + otp);
        boolean bool = emailSenderService.sendEmail(email.getEmail(), email.getSubject(), email.getMessage());
        User user = userService.findUserByEmail(email.getEmail());

        if (bool == true && user != null) {
            System.out.println(user.toString());
            user.setOtp(otp);
            user.setOtpExpiry(new Date());
            userService.updateUser(user);

            return ResponseEntity.ok("Email sent successfully");
        } else
            return ResponseEntity.badRequest().body("Email not sent");
    }

    @PostMapping({ "sendEmail" })
    public ResponseEntity<?> sendEmail(@RequestBody Email email) {
        boolean bool = emailSenderService.sendEmail(email.getEmail(), email.getSubject(), email.getMessage());
        if (bool)
            return ResponseEntity.ok("Email sent successfully");
        else
            return ResponseEntity.badRequest().body("Email not sent");
    }

    @GetMapping({ "verifyOTP/{email}/{otp}" })
    public ResponseEntity<?> verifierOTP(@PathVariable String email, @PathVariable int otp) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            if (user.getOtp() == otp) {
                if (user.getOtpExpiry().toInstant().isAfter(new Date().toInstant().minus(5,
                        ChronoUnit.MINUTES))) {
                    user.setOtp(0);
                    user.setOtpExpiry(null);
                    user.setStatus("ACTIVE");
                    userService.updateUser(user);
                    return ResponseEntity.ok("OTP verified successfully");
                } else
                    return ResponseEntity.badRequest().body("OTP expired");
            } else
                return ResponseEntity.badRequest().body("OTP not verified");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
}
