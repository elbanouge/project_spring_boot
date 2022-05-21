package com.example.creditproject.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

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

    // @Column(name = "username", unique = true)
    // private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

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

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "client")
    private boolean client;

    @Column(name = "fonctionnaire")
    private boolean fonctionnaire;

    @Column(name = "mensuel")
    private String mensuel;

    @Column(name="processInstanceId")
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public boolean isFonctionnaire() {
        return fonctionnaire;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAddress() {
        return address;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean getFonctionnaire() {
        return fonctionnaire;
    }

    public void setFonctionnaire(boolean fonctionnaire) {
        this.fonctionnaire = fonctionnaire;
    }

    public String getMensuel() {
        return mensuel;
    }

    public void setMensuel(String mensuel) {
        this.mensuel = mensuel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDateNai() {
        return dateNai;
    }

    public void setDateNai(String dateNai) {
        this.dateNai = dateNai;
    }

    @Column(name = "dateNai")
    private String dateNai;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [address=" + address + ", cin=" + cin + ", client=" + client + ", dateNai=" + dateNai + ", email="
                + email + ", firstName=" + firstName + ", fonctionnaire=" + fonctionnaire + ", id=" + id + ", lastName="
                + lastName + ", mensuel=" + mensuel + ", nationalite=" + nationalite + ", password=" + password
                + ", phone=" + phone + ", role=" + role + ", sexe=" + sexe + "]";
    }

}
