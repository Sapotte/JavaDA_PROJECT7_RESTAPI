package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user = new User();

    private User oldUser = new User();

    @BeforeEach
    public void setup() {
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");

        oldUser.setId(1);
        oldUser.setUsername("OldUsername");
        oldUser.setPassword("OldPassword");
    }

    @Test
    public void testAddUserOk() {
        // Testing when the user does not already exist in the repository
        when(userRepository.findByUsername("Username")).thenReturn(Optional.empty());
        userService.addUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAddUserAlreadyExistingKo() {
        // Testing when the user already exists in the repository
        when(userRepository.findByUsername("Username")).thenReturn(Optional.of(user));

        // Expect an IllegalArgumentException when trying to add a user that already exists
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        verify(userRepository, times(0)).save(user); // The save method should still only have been called once
    }

    @Test
    public void testUpdateUserOk() {
        when(userRepository.findById(1)).thenReturn(Optional.of(oldUser));
        userService.updateUser(user, 1);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserPasswordNotUpdated() {
        // Testing when the password is null
        user.setPassword(null);

        when(userRepository.findById(1)).thenReturn(Optional.of(oldUser));
        userService.updateUser(user, 1);

        Assertions.assertEquals("OldPassword", oldUser.getPassword());
        Assertions.assertEquals("Username", oldUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserInvalidId() {
        user.setId(2);
        // Expect an IllegalArgumentException when the id is invalid
        Assertions.assertThrows(NullPointerException.class, () -> userService.updateUser(user, 2));
        verify(userRepository, times(0)).save(user);
    }
}
