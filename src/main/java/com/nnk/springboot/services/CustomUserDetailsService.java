package com.nnk.springboot.services;

import com.nnk.springboot.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            LOGGER.error("User not found: " + username);
            throw new UsernameNotFoundException("User not found");
        } else {
            var foundUser = user.get();
            LOGGER.info("Found user: " + foundUser);
            return new org.springframework.security.core.userdetails.User(
                    foundUser.getUsername(),
                    foundUser.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(foundUser.getRole()))
            );
        }
    }
}
