package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock
    BidListRepository bidListRepository;

    @InjectMocks
    BidService bidService;
  
    @Test
    void givenBidLists_whenGetBidLists_thenReturnBidLists() {
        // given
        BidList bidList1 = new BidList("account1", "type1", 1.0);
        BidList bidList2 = new BidList("account2", "type2", 2.0);
        List<BidList> bidListDummyData = new ArrayList<>();
        bidListDummyData.add(bidList1);
        bidListDummyData.add(bidList2);

        // when
        when(bidListRepository.findAll()).thenReturn(bidListDummyData);
        List<BidList> bidListResult = bidService.getBidLists();

        // then
        assertEquals(2, bidListResult.size());
        verify(bidListRepository, times(1)).findAll();
    }
  
    @Test
    void testGetBidListsWhenRepositoryReturnsEmpty() {
        // given
        List<BidList> bidListDummyData = new ArrayList<>();

        // when
        when(bidListRepository.findAll()).thenReturn(bidListDummyData);
        List<BidList> bidListResult = bidService.getBidLists();

        // then
        assertTrue(bidListResult.isEmpty());
        verify(bidListRepository, times(1)).findAll();
    }
    @Test
    void TestAddBidWhenGivenValidBid() {
        // given
        BidList bidList = new BidList("account3", "type3", 3.0);

        // when
        bidService.addBid(bidList);

        // then
        verify(bidListRepository, times(1)).save(bidList);
        assertNotNull(bidList.getCreationDate());
    }

    @Test
    void testAddBidWhenRepositoryThrowsException() {
        // given
        BidList bidList = new BidList("account4", "type4", 4.0);

        // when
        doThrow(IllegalArgumentException.class).when(bidListRepository).save(bidList);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            bidService.addBid(bidList);
        });
    }

    @Test
    void testGetBidByIdWhenBidExists() {
        // given
        Integer id = 1;
        BidList bidList = new BidList("account3", "type3", 3.0);

        // when
        when(bidListRepository.findById(id)).thenReturn(Optional.of(bidList));
        BidList foundBidList = bidService.getBidById(id);

        // then
        assertEquals(bidList, foundBidList);
        verify(bidListRepository, times(1)).findById(id);
   }

   @Test
   void testGetBidByIdWhenBidDoesNotExist() {
       // given
       Integer id = 100;

       // when
       when(bidListRepository.findById(id)).thenReturn(Optional.empty());
       assertThrows(NoSuchElementException.class, () -> {
           bidService.getBidById(id);
       });

       // then
       verify(bidListRepository, times(1)).findById(id);
   }
    @Test
    public void testUpdateBidWhenBidExists() {
        // given
        Integer id = 1;
        BidList bidList = new BidList("account3", "type3", 3.0);

        // when
        doNothing().when(bidListRepository).update(id, bidList.getAccount(), bidList.getType(), bidList.getBidQuantity());
        bidService.updateBid(id, bidList);

        // then
        verify(bidListRepository, times(1)).update(id, bidList.getAccount(), bidList.getType(), bidList.getBidQuantity());
    }

   @Test
   public void testUpdateBidWhenExceptionOccurs() {
        // given
        Integer id = 1;
        BidList bidList = new BidList("account4", "type4", 4.0);

        // when
        doThrow(IllegalArgumentException.class).when(bidListRepository).update(id, bidList.getAccount(), bidList.getType(), bidList.getBidQuantity());

        assertThrows(IllegalArgumentException.class, () -> {
            bidService.updateBid(id, bidList);
        });
    }

    @Test
    void deleteBid_givenExistingId() {
        when(bidListRepository.findById(any())).thenReturn(Optional.of(new BidList("account", "type", 1.0)));
        bidService.deleteBid(1);

        // then
        verify(bidListRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBid_givenNonExistingId() {
        // given
        Integer id = 100;

        // when
        when(bidListRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            bidService.deleteBid(id);
        });

        // then
        verify(bidListRepository, times(0)).deleteById(id);
    }
}
