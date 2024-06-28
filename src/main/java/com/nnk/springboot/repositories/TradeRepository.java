package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;


public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Modifying
    @Query("UPDATE Trade SET account = :account, type = :type, buyQuantity = :buyQuantity, sellQuantity = :sellQuantity, " +
            "buyPrice = :buyPrice, sellPrice = :sellPrice, benchmark = :benchmark, tradeDate = :tradeDate, security = :security, status = :status, " +
            "trader = :trader, book = :book, revisionDate = :revisionDate, dealName = :dealName, dealType = :dealType, sourceListId = :sourceListId, " +
            "side = :side WHERE tradeId = :tradeId")
    void updateTrade(@Param("tradeId") int tradeId, @Param("account") String account, @Param("type") String type, Double buyQuantity, Double sellQuantity, Double buyPrice, Double sellPrice, String benchmark, Instant tradeDate, String security, String status, String trader, String book, Instant revisionDate, String dealName, String dealType, String sourceListId, String side);
}
