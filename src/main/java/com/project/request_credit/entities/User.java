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

import com.fasterxml.jackson.annotation.JsonFormat;
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
        private String sexe;
        @JsonFormat(pattern="dd/MM/yyyy")
        private Date dateNai;
        //private String lieu_naissance;

        // more fields
        private String nationalite;
        private boolean client;
        private boolean fonctionnaire;
        private String mensuel;

        // more fields
        private int otp;
        private Date otpExpiry;
        private String status;
//        private String processInstanceId;
//        private String taskId;
//        private String taskName;

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

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
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

        public void setAddress(String address) {
                this.address = address;
        }

        public String getSexe() {
                return sexe;
        }

        public void setSexe(String sexe) {
                this.sexe = sexe;
        }

        public Date getDateNai() {
                return dateNai;
        }

        public void setDateNai(Date dateNai) {
                this.dateNai = dateNai;
        }

        public String getNationalite() {
                return nationalite;
        }

        public void setNationalite(String nationalite) {
                this.nationalite = nationalite;
        }

        public boolean isClient() {
                return client;
        }

        public void setClient(boolean client) {
                this.client = client;
        }

        public boolean isFonctionnaire() {
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

        public int getOtp() {
                return otp;
        }

        public void setOtp(int otp) {
                this.otp = otp;
        }

        public Date getOtpExpiry() {
                return otpExpiry;
        }

        public void setOtpExpiry(Date otpExpiry) {
                this.otpExpiry = otpExpiry;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }



        public Set<Role> getRoles() {
                return roles;
        }

        public void setRoles(Set<Role> roles) {
                this.roles = roles;
        }

        public Set<Scanner> getScanners() {
                return scanners;
        }

        public void setScanners(Set<Scanner> scanners) {
                this.scanners = scanners;
        }

        public User(Long id, String firstName, String lastName, String username, String password, String email,
                        String phone, String cin, String address, String sexe, Date dateNai, String nationalite,
                        boolean client, boolean fonctionnaire, String mensuel, int otp, Date otpExpiry, String status,
                        Set<Role> roles, Set<Scanner> scanners) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.username = username;
                this.password = password;
                this.email = email;
                this.phone = phone;
                this.cin = cin;
                this.address = address;
                this.sexe = sexe;
                this.dateNai = dateNai;
                this.nationalite = nationalite;
                this.client = client;
                this.fonctionnaire = fonctionnaire;
                this.mensuel = mensuel;
                this.otp = otp;
                this.otpExpiry = otpExpiry;
                this.status = status;
                this.roles = roles;
                this.scanners = scanners;
        }

}
