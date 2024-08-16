package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.Mockito;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

  @Test
  void testLoadUserByUsername_whenUserExists() {
    User user = new User();
    user.setUsername("user");
    user.setPassword("password");
    user.setRole("ROLE_ADMIN");
    UserRepository mockRepository = Mockito.mock(UserRepository.class);
    when(mockRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));
    CustomUserDetailsService service = new CustomUserDetailsService(mockRepository);
    UserDetails result = service.loadUserByUsername("existingUser");
    
    assertEquals("user", result.getUsername());
    assertEquals("password", result.getPassword());
  }
  
  @Test
  void testLoadUserByUsername_whenUserDoesNotExist() {
    UserRepository mockRepository = Mockito.mock(UserRepository.class);
    when(mockRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());
    CustomUserDetailsService service = new CustomUserDetailsService(mockRepository);
    
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("nonexistentUser"));
  }
}