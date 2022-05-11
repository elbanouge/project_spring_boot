package com.example.creditproject.controllers;

import com.example.creditproject.entities.User;
import com.example.creditproject.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/api/user/")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("registration")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println(user.getFirstName());
        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole("user");
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> auth(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) == null) {
            return new ResponseEntity<>("email", HttpStatus.NOT_FOUND);
        }
        User u = userService.findByEmail(user.getEmail());
        if (u.getPassword().equals(user.getPassword()))
            return new ResponseEntity<>(u, HttpStatus.OK);
        return new ResponseEntity<>("password", HttpStatus.NOT_FOUND);

    }

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email, @RequestBody String password) {

        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassworde(password, email),
                HttpStatus.OK);
    }

    @PostMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email) {
        // String password=user.getPassword();
        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassword(email),
                HttpStatus.OK);
    }

    @PostMapping("Personalinfos")
    public ResponseEntity<?> Personalinfos(@RequestBody User user) {
        // System.out.println(user.getFirstName());
        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole("user");
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("createPassword/{email}/{password}")
    public ResponseEntity<?> createPassword(@PathVariable String email, @PathVariable String password) {
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setPassword(password);
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("createPassword/{email}")
    public ResponseEntity<?> createPassword(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setPassword("123456");
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
