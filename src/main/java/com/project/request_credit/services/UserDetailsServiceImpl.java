package com.project.request_credit.services;

import java.security.Principal;

import com.project.request_credit.entities.User;
import com.project.request_credit.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }

    public User profile() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        UserDetails uDetails = loadUserByUsername(principal.getName());
        User user = userRepository.findByUsername(uDetails.getUsername()).orElse(null);
        return user;
    }

}
