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

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a User by their username.
     *
     * @param username the username of the User to load
     * @return the UserDetails object representing the User
     * @throws UsernameNotFoundException if the User is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user  = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            LOGGER.info("Found user: " + user);
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_"+user.get().getRole()))
            );
        }
    }
}
