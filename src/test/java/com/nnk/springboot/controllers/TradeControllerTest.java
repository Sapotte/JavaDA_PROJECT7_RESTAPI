package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeControllerTest {

    @InjectMocks
    private TradeController tradeController;

    @Mock
    private TradeService tradeService;


    @Test
    public void testHomeMethod_Success() {
        // Arrange
        Model model = new BindingAwareModelMap();

        // Act
        String viewName = tradeController.home(model);

        // Assert
        assertEquals("trade/list", viewName);
    }

    @Test
    public void testAddUserMethod_Success() {
        // Arrange
        Trade trade = new Trade();

        // Act
        ModelAndView mav = tradeController.addUser(trade);

        // Assert
        assertEquals("trade/add", mav.getViewName());
        assertSame(trade, mav.getModel().get("trade"));
    }
    @Test
    public void testValidateMethodWithoutErrors() {
        // Arrange
        Trade trade = new Trade();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new BindingAwareModelMap();

        // Act
        doNothing().when(tradeService).createTrade(any(Trade.class));
        String viewName = tradeController.validate(trade, bindingResult, model);

        // Assert
        verify(tradeService, times(1)).createTrade(any(Trade.class));
        assertEquals("redirect:/trade/list", viewName);
    }

    @Test
    public void testValidateMethodWithErrors() {
        // Arrange
        Trade trade = new Trade();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new BindingAwareModelMap();

        // Act
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = tradeController.validate(trade, bindingResult, model);

        // Assert
        verify(tradeService, times(0)).createTrade(any(Trade.class));
        assertEquals("trade/add", viewName);
    }
    @Test
    public void testShowUpdateForm() {
        // Arrange
        Trade trade = new Trade();
        int id = 1;
        Model model = new BindingAwareModelMap();

        // Act
        when(tradeService.findTradeById(id)).thenReturn(trade);
        String viewName = tradeController.showUpdateForm(id, model);

        // Assert
        verify(tradeService, times(1)).findTradeById(id);
        assertSame(trade, model.getAttribute("trade"));
        assertEquals("trade/update", viewName);
    }
    @Test
    public void testUpdateTradeMethodWithoutErrors() {
        // Arrange
        Trade trade = new Trade();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new BindingAwareModelMap();
        Integer id = 1;

        // Act
        doNothing().when(tradeService).updateTrade(id, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
        String viewName = tradeController.updateTrade(id, trade, bindingResult, model);

        // Assert
        verify(tradeService, times(1)).updateTrade(id, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
        assertEquals("redirect:/trade/list", viewName);
    }

    @Test
    public void testUpdateTradeMethodWithErrors() {
        // Arrange
        Trade trade = new Trade();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = new BindingAwareModelMap();
        Integer id = 1;

        // Act
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = tradeController.updateTrade(id, trade, bindingResult, model);

        // Assert
        verify(tradeService, times(0)).updateTrade(id, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
        assertEquals("trade/update", viewName);
    }
    @Test
    public void testDeleteTradeMethod() {
        // Arrange
        Integer id = 1;
        Model model = new BindingAwareModelMap();

        // Act
        doNothing().when(tradeService).deleteTrade(id);
        String viewName = tradeController.deleteTrade(id, model);

        // Assert
		verify(tradeService, times(1)).deleteTrade(id);
		assertEquals("redirect:/trade/list", viewName);
    }
}
