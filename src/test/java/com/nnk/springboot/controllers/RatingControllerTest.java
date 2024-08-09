package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RatingControllerTest {
    @InjectMocks
    RatingController ratingController;

    @Mock
    RatingService ratingService;

    @Mock
    RatingRepository ratingRepository;

    private MockMvc mockMvc;

    @BeforeEach
public void setUp() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
    Rating rating = new Rating();
    rating.setId(1);
    List<Rating> ratings = new ArrayList<>();
    ratings.add(rating);
    when(ratingRepository.findAll()).thenReturn(ratings);
    mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
}

@Test
public void home() throws Exception {
    mockMvc.perform((MockMvcRequestBuilders.get("/rating/list")))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("ratings","username"))
        .andExpect(view().name("rating/list"));
}
}
