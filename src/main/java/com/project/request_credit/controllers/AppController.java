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

            Role role1 = new Role(null, "MANAGER");
            Role role2 = new Role(null, "CONSEILLER");
            Role role3 = new Role(null, "CLIENT");

            accountService.createNewRole(role1);
            accountService.createNewRole(role2);
            accountService.createNewRole(role3);

            User user1 = new User(null, "Abdellah", "Elbanouge", "abde.banouge2",
                    "abde24", "abde.banouge2@gmail.com",
                    "0615761546", "JE295844", "89 BLOC E LOT TIZNIT", true,
                    "24/12/1995", "OUIJJANE TIZNIT", null,
                    null, false, false, null, 0, null, null, new HashSet<>(), new HashSet<>());

            User user2 = new User(null, "Ilyas", "Haman", "ilyas.haman",
                    "ilyas", "ilyas.haman@gmail.com",
                    "0615761546", null, null,
                    true, null, null, null,
                    null, false, false, null, 0, null, null, new HashSet<>(), new HashSet<>());

            User user3 = new User(null, "ADMIN", "ADMIN", "admin",
                    "admin", "admin@gmail.com",
                    null, null, null,
                    true, null, null, null,
                    null, false, false, null, 0, null, null, new HashSet<>(), new HashSet<>());

            accountService.createNewUser(user1);
            accountService.createNewUser(user2);
            accountService.createNewUser(user3);

            accountService.addRoleToUser("admin", "MANAGER");

        } catch (Exception e) {
            System.out.println("Error while creating" + e.getMessage());
        }
    }
}
