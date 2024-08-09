package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListControllerTest {
    @InjectMocks
    BidListController bidListController;

    @Mock
    BidService bidService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
    }

    @Test
    void home() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"));
        verify(bidService, Mockito.times(1)).getBidLists();
    }

    @Test
    void addBidForm() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add"));
    }

    @Test
    void validateSuccess() throws Exception {
        BidList newBid = new BidList("accountNew", "typeNew", 10.0);
        when(bidService.addBid(any())).thenReturn(newBid);
        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", newBid))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"));
        verify(bidService, Mockito.times(1)).addBid(any());
    }

    @Test
    void validateFailure() throws Exception {
        BidList invalidBid = new BidList("", "", null);
        when(bidService.addBid(any())).thenThrow(InvalidDataAccessApiUsageException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", invalidBid))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add?errorDB"));
        verify(bidService, Mockito.times(1)).addBid(any());
    }

    @Test
    void deleteBid() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
        doNothing().when(bidService).deleteBid(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    void showUpdateForm() throws Exception {
        BidList expectedBid = new BidList("accountUpdate", "typeUpdate", 20.0);
        when(bidService.getBidById(anyInt())).thenReturn(expectedBid);
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("bidList", expectedBid));
    }

    @Test
    void updateBidSuccess() throws Exception {
        BidList updatedBid = new BidList("accountUpdated", "typeUpdated", 30.0);
        doNothing().when(bidService).updateBid(anyInt(), any());
        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                .flashAttr("bidList", updatedBid))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
        verify(bidService, times(1)).updateBid(anyInt(), any());
    }
}
