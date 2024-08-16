package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TradeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TradeController tradeController;

    @Mock
    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication auth = new TestingAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    public void home_ShouldAddTradeDataToModelAndRenderHomeView() throws Exception {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade());

        when(tradeService.findAllTrades()).thenReturn(trades);

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", is(trades)));
    }

    @Test
    public void addUser_ShouldAddTradeDataToModelAndView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    public void validate_ShouldPerformRedirectionWhenNoBindingErrors() throws Exception {
        Trade trade = new Trade();

        when(tradeService.createTrade(any(Trade.class))).thenReturn(trade);

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("account", "Test Account")
                    .param("type", "Test Type")
                    .param("buyQuantity", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    public void validate_ShouldReturnToAddTradeViewWhenThereAreBindingErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                .param("account", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("trade"))
                .andExpect(model().attributeHasFieldErrors("trade", "account"))
                .andExpect(view().name("trade/add"));
    }
    @Test
    public void showUpdateForm_ShouldAddTradeToModelAndRenderUpdateView() throws Exception {
        Trade expectedTrade = new Trade();
        expectedTrade.setAccount("Test Account");
        expectedTrade.setType("Test Type");
        expectedTrade.setBuyQuantity(100.0);

        when(tradeService.findTradeById(any(Integer.class))).thenReturn(expectedTrade);

        mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", is(expectedTrade)));
    }
    @Test
    public void updateTrade_ShouldPerformRedirectionWhenNoBindingErrors() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("Test Account");
        trade.setType("Test Type");
        trade.setBuyQuantity(100.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                    .param("account", trade.getAccount())
                    .param("type", trade.getType())
                    .param("buyQuantity", trade.getBuyQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    public void updateTrade_ShouldReturnToUpdateTradeViewWhenThereAreBindingErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("account", "")
                    .param("type", "Test Type")
                    .param("buyQuantity", "100.0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("trade"))
                .andExpect(model().attributeHasFieldErrors("trade", "account"))
                .andExpect(view().name("trade/update"));
    }

    @Test
    public void deleteTrade_ShouldPerformRedirectionAfterDeletion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }
}
