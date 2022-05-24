package com.project.request_credit.services;

import java.util.List;
import java.util.Set;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.Role;
import com.project.request_credit.entities.User;
import com.project.request_credit.repositories.CreditRepository;
import com.project.request_credit.repositories.RoleRepository;
import com.project.request_credit.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CreditRepository creditRepository;

    @Override
    public Role createNewRole(Role role) {
        Role roleExist = roleRepository.findRoleByName(role.getName());
        if (roleExist != null) {
            return roleExist;
        } else {
            return roleRepository.save(role);
        }
    }

    @Override
    public User createNewUser(User user) {
        User userExist = findUserByUsername(user.getUsername());
        if (userExist == null) {
            Role userRole = roleRepository.findRoleByName("USER");
            user.setRoles((Set.of(userRole)));
            user.setStatus("DISABLED");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        } else {
            return userExist;
        }
    }

    @Override
    public boolean updateUser(User user) {
        boolean bol = false;
        User userExist = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userExist != null) {
            user.setId(userExist.getId());
            user.setEmail(userExist.getEmail());
            user.setUsername(userExist.getUsername());
            user.setRoles(userExist.getRoles());
            user.setScanners(userExist.getScanners());
            userRepository.save(user);
            bol = true;
        }
        return bol;
    }

    @Override
    public boolean addRoleToUser(String username, String name) {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role = roleRepository.findRoleByName(name);
        if (user != null && role != null) {
            if (user.getRoles().add(role)) {
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User updatePassword(String password, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        Credit credit = creditRepository.findByUser(user);
        // Credit credit = new Credit();
        if (user != null && credit != null) {
            creditRepository.delete(credit);
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}
