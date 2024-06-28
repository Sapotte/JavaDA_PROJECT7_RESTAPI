package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {
    private Logger logger = LogManager.getLogger(TradeService.class);
    @Autowired
    TradeRepository tradeRepository;

    public List<Trade> findAllTrades() {
        return tradeRepository.findAll();
    }

    public Trade findTradeById(int id) {
        return tradeRepository.findById(id).orElseThrow(()-> new RuntimeException("Could not find trade with id: " + id));
    }

    public Trade createTrade(Trade curvePoint) {
        return tradeRepository.save(curvePoint);
    }

    public void updateTrade(Integer id, String account, String type, Double buyQuantity,
                            Double sellQuantity, Double buyPrice, Double sellPrice, String benchmark,
                            Instant tradeDate, String security, String status, String trader, String book,
                            String dealName, String dealType, String sourceListId, String side) {
        if(!tradeRepository.existsById(id)) {
            throw new RuntimeException("Could not find trade with id: " + id);
        }
        tradeRepository.updateTrade(id, account, type, buyQuantity, sellQuantity, buyPrice, sellPrice, benchmark,
                tradeDate, security, status, trader, book, Instant.now(), dealName, dealType, sourceListId, side);
    }

    public void deleteTrade(Integer id) {
        if(!tradeRepository.existsById(id)) {
            throw new RuntimeException("Could not find trade with id: " + id);
        }
        tradeRepository.deleteById(id);
    }
}
