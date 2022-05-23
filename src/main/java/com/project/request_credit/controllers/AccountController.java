package com.project.request_credit.controllers;

import java.util.List;

import com.project.request_credit.entities.Role;
import com.project.request_credit.entities.User;
import com.project.request_credit.models.UserRole;
import com.project.request_credit.services.AccountService;
import com.project.request_credit.services.UserDetailsServiceImpl;

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

@RequestMapping("/api/user/")
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping({ "createNewRole" })
    public ResponseEntity<?> createNewRole(@RequestBody Role role) {
        Role roleExist = accountService.findRoleByName(role.getName());
        if (roleExist != null) {
            return new ResponseEntity<>("Role already exist", HttpStatus.BAD_REQUEST);
        } else {
            Role newRole = accountService.createNewRole(role);
            return new ResponseEntity<>(newRole, HttpStatus.OK);
        }
    }

    @PostMapping({ "addRoleToUser" })
    public ResponseEntity<?> addRoleToUser(@RequestBody UserRole userRole) {
        boolean bol = accountService.addRoleToUser(userRole.getUserName(), userRole.getRoleName());
        if (bol == true) {
            return new ResponseEntity<>("Role added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role not added", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({ "all" })
    public List<User> getAllUsers() {
        return accountService.getAllUsers();
    }

    @PostMapping({ "login" })
    public ResponseEntity<?> auth(@RequestBody User user) {
        User userFromDb = accountService.findUserByEmail(user.getEmail());
        if (userFromDb != null) {
            if (userFromDb.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(userFromDb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Password is incorrect", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping({ "passwordForgot" })
    public ResponseEntity<?> find(@RequestBody User user) {
        User userUpdated = accountService.updatePassword(user.getPassword(), user.getEmail());
        if (userUpdated != null) {
            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping({ "registration" })
    public ResponseEntity<?> Personalinfos(@RequestBody User user) {
        User userExist = accountService.findUserByEmail(user.getEmail());
        User userExistUsername = accountService.findUserByUsername(user.getUsername());
        if (userExist != null) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        } else if (userExistUsername != null) {
            return new ResponseEntity<>("Username is already in use!", HttpStatus.BAD_REQUEST);
        } else {
            User userCreated = accountService.createNewUser(user);
            return new ResponseEntity<>(userCreated, HttpStatus.OK);
        }
    }

    @GetMapping("findByEmail/{email}")
    public ResponseEntity<?> find(@PathVariable String email) {
        User user = accountService.findUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PutMapping({ "createPassword/{email}/{password}" })
    public ResponseEntity<?> createPassword(@PathVariable String email, @PathVariable String password) {
        User userUpdated = accountService.updatePassword(password, email);
        if (userUpdated != null) {
            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping({ "/profile" })
    public User profile() {
        return userDetailsService.profile();
    }

    @DeleteMapping({ "delete/{id}" })
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean bol = accountService.deleteUser(id);
        if (bol == true) {
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not deleted", HttpStatus.BAD_REQUEST);
        }
    }
}
