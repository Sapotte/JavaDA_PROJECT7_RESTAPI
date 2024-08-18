package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void home_showsListOfUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());

        given(userRepository.findAll()).willReturn(users);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users));
    }
    @Test
    public void addUser_test() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void validate_testWithValidUser() throws Exception {
        User user = new User();
        user.setUsername("TestName");
        user.setFullname("TestFullName");
        user.setPassword("TestPassword1!");
        user.setRole("ROLE_ADMIN");

        mockMvc.perform(post("/user/validate")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
    }
    @Test
    public void validate_testWithInvalidUser() throws Exception {
        User user = new User();
        user.setUsername("");
        user.setPassword("");
        mockMvc.perform(post("/user/validate")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    public void validate_testWithValidUserAndExistingUsername() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .flashAttr("user", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    public void showUpdateForm_testWithValidId() throws Exception {
        User user = new User();
        user.setUsername("TestName");
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }

    // Test updateUser method, assuming valid user info.
    @Test
    public void updateUser_validUserTest() throws Exception {
        User user = new User();
        user.setFullname("Full");
        user.setUsername("TestName");
        user.setPassword("TestPassword1!");
        user.setRole("ROLE_ADMIN");

        mockMvc.perform(post("/user/update/1")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
    }

    // Test updateUser method when faced with invalid user info.
    @Test
    public void updateUser_invalidUserTest() throws Exception {
        User user = new User();

        mockMvc.perform(post("/user/update/1")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    // Test deleteUser method for a valid user ID.
    @Test
    public void deleteUser_validIdTest() throws Exception {
        User user = new User();
        user.setId(1);
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        doNothing().when(userRepository).delete(user);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
    }
}
