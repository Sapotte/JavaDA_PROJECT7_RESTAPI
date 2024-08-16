package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; 
import org.mockito.InjectMocks; 
import org.mockito.Mock; 
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    public void findAllTradesTest() {
        //given
        List<Trade> tradesList = new ArrayList<>();
        Trade trade = new Trade("test1", "testType");
        tradesList.add(trade);
        given(tradeRepository.findAll()).willReturn(tradesList);
        
        //when
        List<Trade> expected = tradeService.findAllTrades();
        
        //then
        assertThat(expected).isEqualTo(tradesList);
    }
    @Test
    public void findTradeByIdTest_success() {
        //given
        Trade trade = new Trade("test1", "testType");
        given(tradeRepository.findById(1)).willReturn(Optional.of(trade));

        //when
        Trade expected = tradeService.findTradeById(1);

        //then
        assertThat(expected).isEqualTo(trade);
    }
    @Test
    public void findTradeByIdTest_failure() {
        //given
        given(tradeRepository.findById(1)).willReturn(Optional.empty());

        //when / then
        assertThrows(RuntimeException.class, () -> tradeService.findTradeById(1));
    }
    @Test
    public void createTradeTest_success() {
        //given
        Trade trade = new Trade("test2", "testType2");
        given(tradeRepository.save(any(Trade.class))).willReturn(trade);

        //when
        Trade expected = tradeService.createTrade(trade);

        //then
        assertThat(expected).isEqualTo(trade);
    }

    @Test
    public void createTradeTest_failure() {
        //given
        Trade trade = new Trade("test2", "testType2");
        given(tradeRepository.save(any(Trade.class))).willThrow(new IllegalArgumentException("Invalid argument"));

        //when / then
        assertThrows(IllegalArgumentException.class, () -> tradeService.createTrade(trade));
    }
    @Test
    public void updateTradeTest_success() {
        //given
        Integer id = 1;
        String account = "acc1";
        String type = "type1";
        Double buyQuantity = 100.0;
        given(tradeRepository.existsById(id)).willReturn(true);

        //when
        tradeService.updateTrade(id, account, type, buyQuantity);

        verify(tradeRepository, times(1)).updateTrade(eq(1), any(), any(), any(), any());
    }

    @Test
    public void updateTradeTest_failure() {
        //given
        Integer id = 1;
        String account = "acc1";
        String type = "type1";
        Double buyQuantity = 100.0;
        Trade trade = new Trade(account, type);
        given(tradeRepository.existsById(id)).willReturn(false);

        //when / then
        assertThrows(RuntimeException.class, () -> tradeService.updateTrade(id, account, type, buyQuantity));
    }
    @Test
    public void deleteTradeTest_success() {
        //given
        Integer id = 1;
        given(tradeRepository.existsById(id)).willReturn(true);

        //when
        tradeService.deleteTrade(id);

        //then
        // Cannot assert as the method is void.
    }

    @Test
    public void deleteTradeTest_failure() {
        //given
        Integer id = 1;
        given(tradeRepository.existsById(id)).willReturn(false);

        //when / then
        assertThrows(RuntimeException.class, () -> tradeService.deleteTrade(id));
    }
}
