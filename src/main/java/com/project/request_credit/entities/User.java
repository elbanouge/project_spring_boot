package com.project.request_credit.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;
        private String firstName;
        private String lastName;
        @Column(name = "username", unique = true)
        private String username;
        private String password;
        @Column(name = "email", unique = true)
        private String email;
        private String phone;
        private String cin;
        private String address;
        private Boolean sexe;
        private String date_naissance;
        private String lieu_naissance;

        // more fields
        private String nationalite;
        private boolean client;
        private boolean fonctionnaire;
        private String mensuel;

        // more fields
        private int otp;
        private Date otpExpiry;
        private String status;
        private String processInstanceId;
        private String taskId;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "users_roles", joinColumns = {
                        @JoinColumn(name = "user_id")
        }, inverseJoinColumns = {
                        @JoinColumn(name = "role_id")
        })
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Set<Role> roles = new HashSet<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "users_scanners", joinColumns = {
                        @JoinColumn(name = "user_id")
        }, inverseJoinColumns = {
                        @JoinColumn(name = "scanner_id")
        })
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Set<Scanner> scanners = new HashSet<>();
}
