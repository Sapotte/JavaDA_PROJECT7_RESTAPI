package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BidListRepository extends JpaRepository<BidList, Integer> {
    @Modifying
    @Query("UPDATE BidList bl SET bl.account = :account, bl.type = :type, bl.bidQuantity = :bidQuantity WHERE bl.bidListId = :id")
    void update(@Param("id") Integer id, @Param("account") String account, @Param("type") String type, @Param("bidQuantity") Double bidQuantity);
}
