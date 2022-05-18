package com.example.creditproject.controllers;

import com.example.creditproject.entities.User;
import java.util.List;

import com.example.creditproject.entities.User;
import com.example.creditproject.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user/")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("registration")
    public ResponseEntity<?> register(@RequestBody User user) {
        //System.out.println(user.getFirstName());
        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole("user");
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // @GetMapping("login/{email}")
    // public ResponseEntity<?> auth(@PathVariable String email) {
    // if (userService.findByEmail(email) == null) {
    // return ResponseEntity.notFound().build();
    // }
    // return new ResponseEntity<>(
    // userService.findByEmail(email),
    // HttpStatus.OK);
    // }

    @PostMapping("login")
    public ResponseEntity<?> auth(@RequestBody User user) {
        //
        if (userService.findByEmail(user.getEmail())==null) {
            return new ResponseEntity<>("email", HttpStatus.NOT_FOUND);
        } else {
            User userFromDb = userService.findByEmail(user.getEmail());
            if(userFromDb.getPassword().equals(user.getPassword())==false)
                return new ResponseEntity<>("password", HttpStatus.NOT_FOUND);
            else return new ResponseEntity<>(userFromDb, HttpStatus.OK);
        }
    }

    @GetMapping("all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();// ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email, @RequestBody String password) {
        // String password=user.getPassword();

        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassword(password, email),
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
        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole("user");
        // user.setPassword("admin");
        userService.save(user);
        System.out.println(user.toString());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{email}")
    public ResponseEntity<?> delete(@PathVariable String email){
        userService.deleteByEmail(email);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteAll(){
        userService.deleteAll();
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PutMapping("createPassword/{email}")
    public ResponseEntity<?> createPassword(@PathVariable String email, @RequestBody String password) {
        // String password=user.getPassword();

        if (userService.findByEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(
                userService.updatePassword(password, email),
                HttpStatus.OK);
    }

    @PutMapping("getUserData/{email}")
    public ResponseEntity<User> getUserData(@PathVariable String email) {
        User user=userService.findByEmail(email);
        if(user!=null)
            return new ResponseEntity<>(user ,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
