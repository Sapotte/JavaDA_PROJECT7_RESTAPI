package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testAddUser() {
        UserService userService = new UserService(userRepository);
        User user = new User();
        user.setUsername("Username");
        user.setPassword("Password");

        // Testing when the user does not already exist in the repository
        when(userRepository.findByUsername("Username")).thenReturn(Optional.empty());
        userService.addUser(user);
        verify(userRepository, times(1)).save(user);

        // Testing when the user already exists in the repository
        User existingUser = new User();
        user.setUsername("Username");
        when(userRepository.findByUsername("Username")).thenReturn(Optional.of(existingUser));

        // Expect an IllegalArgumentException when trying to add a user that already exists
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        verify(userRepository, times(1)).save(user); // The save method should still only have been called once
    }

    @Test
    public void testUpdateUser() {
        UserService userService = new UserService(userRepository);
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");

        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setUsername("OldUsername");
        oldUser.setPassword("OldPassword");

        when(userRepository.findById(1)).thenReturn(Optional.of(oldUser));
        userService.updateUser(user, 1);

        // Testing when the password is null
        user.setPassword(null);
        userService.updateUser(user, 1);
        verify(userRepository, times(2)).save(user);
        Assertions.assertEquals("OldPassword", user.getPassword());

        // Testing when the password is blank
        user.setPassword("");
        userService.updateUser(user, 1);
        verify(userRepository, times(3)).save(user);
        Assertions.assertEquals("OldPassword", user.getPassword());

        // Expect an IllegalArgumentException when the id is invalid
        Assertions.assertThrows(NullPointerException.class, () -> userService.updateUser(user, 2));
        verify(userRepository, times(3)).save(user);
    }
}
