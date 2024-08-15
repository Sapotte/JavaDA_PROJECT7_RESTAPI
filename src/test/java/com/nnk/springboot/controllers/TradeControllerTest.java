package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TradeControllerTest {
    @InjectMocks
    private TradeController controller;

    @Mock
    private TradeService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth = new TestingAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testHomeWithTrades() throws Exception {
        List<Trade> trades = Arrays.asList(new Trade(), new Trade());
        Mockito.when(service.findAllTrades()).thenReturn(trades);
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("trades", trades))
                .andExpect(MockMvcResultMatchers.view().name("trade/list"));
        verify(service, times(1)).findAllTrades();
    }

    @Test
    public void testHomeWithNoTrades() throws Exception {
        Mockito.when(service.findAllTrades()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.view().name("trade/list"));
        verify(service, times(1)).findAllTrades();
    }


    @Test
    public void testAddUser() throws Exception {
        Trade trade = new Trade();
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("trade", trade))
                .andExpect(MockMvcResultMatchers.view().name("trade/add"));
    }
    @Test
    public void testValidateWithErrors() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("");
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/add"));
    }

    @Test
    public void testValidateWithNoErrors() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("testAccount");
        Mockito.doNothing().when(service).createTrade(trade);
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
        Mockito.verify(service, Mockito.times(1)).createTrade(trade);
    }
    @Test
    public void testShowUpdateForm() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        Mockito.when(service.findTradeById(1)).thenReturn(trade);
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("trade", trade))
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    public void testUpdateTradeSuccessful() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("testAccount");
        Mockito.doNothing().when(service).updateTrade(1, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
        Mockito.verify(service, Mockito.times(1)).updateTrade(1, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
    }

    @Test
    public void testUpdateTradeUnsuccessful() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("");
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    public void testDeleteTradeSuccessful() throws Exception {
        Mockito.doNothing().when(service).deleteTrade(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
        Mockito.verify(service, Mockito.times(1)).deleteTrade(1);
    }

    @Test
    public void testDeleteTradeFailure() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(service).deleteTrade(22);
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/22"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
        Mockito.verify(service, Mockito.times(1)).deleteTrade(22);
    }
}
