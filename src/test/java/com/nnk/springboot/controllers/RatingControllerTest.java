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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RatingControllerTest {
    @InjectMocks
    RatingController ratingController;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    RatingService ratingService;

    private MockMvc mockMvc;

    @BeforeEach
public void setUp() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
    mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
}

@Test
public void home() throws Exception {
    mockMvc.perform((MockMvcRequestBuilders.get("/rating/list")))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("ratings","username"))
        .andExpect(view().name("rating/list"));
}
    @Test
    public void addRatingForm() throws Exception {
        mockMvc.perform((MockMvcRequestBuilders.get("/rating/add")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/add"));
    }
   @Test
   public void validate_Success() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
           .param("moodysRating", "MoodysRating")
           .param("sandPRating", "SandPRating")
           .param("fitchRating", "FitchRating")
           .param("orderNumber", "1"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/rating/list"));
   }

   @Test
   public void showUpdateForm_ValidId() throws Exception{
     Rating testRating = new Rating();
     testRating.setId(1);
     when(ratingRepository.findById(1)).thenReturn(Optional.of(testRating));

     mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))
         .andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.model().attribute("rating", testRating))
         .andExpect(view().name("rating/update"));
   }

   @Test
   public void showUpdateForm_InvalidId() throws Exception {
     when(ratingRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.empty());

     mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))
         .andExpect(status().isOk())
         .andExpect(model().attribute("message", "Rating not found"))
         .andExpect(view().name("rating/list"));
   }

    @Test
    public void updateRating_Failure() throws Exception {
        doThrow(NullPointerException.class).when(ratingService).updateRating(any(), any(), any(), any(), any());
        mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .param("moodysRating", "MoodysRating")
                        .param("sandPRating", "SandPRating")
                        .param("fitchRating", "FitchRating")
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update/{id}?error"));
    }

    @Test
    public void updateRating_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .param("moodysRating", "MoodysRating")
                        .param("sandPRating", "SandPRating")
                        .param("fitchRating", "FitchRating")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }
    @Test
    public void deleteRating_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

    @Test
    public void deleteRating_Failure() throws Exception {
        doThrow(RuntimeException.class).when(ratingService).deleteRating(any());
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list?errorDelete"));
    }
}

