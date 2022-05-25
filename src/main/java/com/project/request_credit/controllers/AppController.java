package com.project.request_credit.controllers;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import com.project.request_credit.entities.Role;
import com.project.request_credit.entities.User;
import com.project.request_credit.services.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppController {

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void init() {
        System.out.println("AccountController initialized");
        // SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Role role1 = new Role(null, "ADMIN");
            Role role2 = new Role(null, "USER");
            accountService.createNewRole(role1);
            accountService.createNewRole(role2);

            User user = new User(null, "Abdellah", "Elbanouge", "abde.banouge2",
                    "abde24", "abde.banouge2@gmail.com",
                    "0615761546", "JE295844", "89 BLOC E LOT TIZNIT", true,
                    "24/12/1995", "OUIJJANE TIZNIT",
                    null, false, false, null, 0, null, null, null, null, new HashSet<>(), new HashSet<>());
            User user2 = new User(null, "Abdellah", "Elbanouge", "abde.banouge3",
                    "abde23", "abde.banouge3@gmail.com",
                    "0615761546", "JE295844", "89 BLOC E LOT TIZNIT",
                    true, "24/12/1995", "OUIJJANE TIZNIT",
                    null, false, false, null, 0, null, null, null, null, new HashSet<>(), new HashSet<>());
            accountService.createNewUser(user);
            accountService.createNewUser(user2);

        } catch (Exception e) {
            System.out.println("Error while creating" + e.getMessage());
        }
    }
}
