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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
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
        BidList validBid = new BidList("Account1", "Type1", 20.0);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", validBid)
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
        verify(bidService, Mockito.times(1)).addBid(any());
    }

    @Test
    void validateFailureWithValidationErrors() throws Exception {
        BidList bidList = new BidList("", "", null);

        BindingResult bindingResult = new BeanPropertyBindingResult(bidList, "bidList");
        bindingResult.rejectValue("account", "error.account", "This field is required");
        bindingResult.rejectValue("type", "error.type", "This field is required");

        mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("org.springframework.validation.BindingResult.bidList", bindingResult)
                        .flashAttr("bidList", bidList))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add"));
        verify(bidService, Mockito.times(0)).addBid(any());
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
