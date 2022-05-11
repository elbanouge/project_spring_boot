package com.example.creditproject.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "otp")
    private int otp;

    @Column(name = "otp_expiry")
    private Date otpExpiry;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cin")
    private String cin;

    @Column(name = "address")
    private String address;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "dateNai")
    private String dateNai;

    @Column(name = "lieuNai")
    private String lieuNai;

    public User() {
        super();
    }

}
