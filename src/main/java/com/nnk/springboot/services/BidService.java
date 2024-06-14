package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.utils.RegexValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BidService {
    @Autowired
    RegexValidation validation;

    @Autowired
    BidListRepository bidListRepository;


    public List<BidList> getBidLists(){
        return bidListRepository.findAll();
    }

    public BidList addBid(BidList bid) {
        try {
            bid.setCreationDate(Instant.now());
            bidListRepository.save(bid);
            return bid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving bid: " + e.getMessage());
        }
    }

    public BidList getBidById(Integer id) {
        return  bidListRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void updateBid(Integer id, BidList bidList) {
        try {
            bidList.setRevisionDate(Instant.now());
            bidListRepository.update(id, bidList.getAccount(), bidList.getType(), bidList.getBidQuantity());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving bid: " + e.getMessage());
        }
    }

    public void deleteBid(Integer id) {
        bidListRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Bid not found"));
        try {
            bidListRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting bid: " + e.getMessage());
        }
    }
}
