package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a new user to the user repository.
     * The user's password is encrypted before saving to the repository using the BCryptPasswordEncoder.
     *
     * @param user the user object to be added to the repository
     */
    public void addUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Updates an existing user in the user repository.
     * If the password is blank or null, the method retains the user's old password.
     * If the password is provided, it is encrypted using the BCryptPasswordEncoder
     * before saving to the repository.
     *
     * @param user the updated user object
     * @param id   the id of the user to be updated
     * @throws IllegalArgumentException if the user id is invalid
     */
    public void updateUser(User user, int id) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if(user.getPassword().isBlank() || user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setId(id);
        userRepository.save(user);
    }
}
