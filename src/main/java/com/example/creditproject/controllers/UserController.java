package com.example.creditproject.controllers;

import com.example.creditproject.models.User;
import com.example.creditproject.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("login/{email}")
    public ResponseEntity<?> auth(@PathVariable String email) {
        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.findByEmail(email),
                HttpStatus.OK);
    }

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();//ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email,@RequestBody String password) {
        //String password=user.getPassword();

        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassworde(password,email),
                HttpStatus.OK);
    }

    @PostMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email) {
        //String password=user.getPassword();
        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassword(email),
                HttpStatus.OK);
    }
}
