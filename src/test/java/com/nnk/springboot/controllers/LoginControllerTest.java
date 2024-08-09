package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    LoginController loginController;

    @Mock
    BidListController bidListController;

    @Mock
    UserController userController;

    @Test
    void homeAsAdmin() {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);

      loginController.home();

      verify(userController, times(1)).home();
    }

    @Test
    void homeAsUser() {
      Authentication auth = new TestingAuthenticationToken("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
      SecurityContextHolder.getContext().setAuthentication(auth);

      loginController.home();

      verify(bidListController,times(1)).home();
    }

    @Mock
    UserRepository userRepository;

    @Test
    void getAllUserArticles() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        ModelAndView mav = loginController.getAllUserArticles();

        assertEquals("user/list", mav.getViewName());
        assertSame(users, mav.getModel().get("users"));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void error_pageTest() {
        ModelAndView mav = loginController.error();
        assertEquals("403", mav.getViewName());
    }
}