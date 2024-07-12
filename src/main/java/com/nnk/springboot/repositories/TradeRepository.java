package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;


public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Trade SET account = :account, type = :type, buyQuantity = :buyQuantity, revisionDate = :revisionDate WHERE tradeId = :tradeId")
    void updateTrade(@Param("tradeId") int tradeId, @Param("account") String account, @Param("type") String type, Double buyQuantity, Instant revisionDate);
}
