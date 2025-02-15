package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDTO;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidListServiceTests {
    @Mock
    private BidListRepository bidListRepository;
    @InjectMocks
    private BidListService bidListService;

    private BidList bidList1;
    private BidList bidList2;
    private BidListDTO bidListDTO;

    @BeforeEach
    void setUp() {
        bidList1 = new BidList();
        bidList1.setId(1);
        bidList1.setAccount("TestAccount");
        bidList1.setType("TestType");
        bidList1.setBidQuantity(10.0);

        bidList2 = new BidList();
        bidList2.setId(2);
        bidList2.setAccount("TestAccount2");
        bidList2.setType("TestType2");
        bidList2.setBidQuantity(20.0);

        bidListDTO = new BidListDTO();
        bidListDTO.setAccount("NewAccount");
        bidListDTO.setType("NewType");
        bidListDTO.setBidQuantity(15.0);
    }

    @Test
    void testGetBidLists() {
        when(bidListRepository.findAll()).thenReturn(List.of(bidList1, bidList2));

        List<BidListDTO> result = bidListService.getBidLists();

        assertEquals(2, result.size());
        assertEquals("TestAccount", result.get(0)
                                          .getAccount());
        verify(bidListRepository, times(1)).findAll();
    }

    @Test
    void testGetBidList_Success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));
        BidList result = bidListService.getBidList(1);

        assertNotNull(result);
        assertEquals("TestAccount", result.getAccount());
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    void testGetBidList_Failure() {
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> bidListService.findBidListToUpdate(1));

        assertEquals("404 NOT_FOUND \"La bidList avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testFindBidListToUpdate_Success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));

        BidListDTO result = bidListService.findBidListToUpdate(1);

        assertNotNull(result);
        assertEquals("TestAccount", result.getAccount());
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    void testAddBidList_Success() {
        bidListService.addBidList(bidListDTO);
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void testAddBidList_Failure() {
        bidListService.addBidList(bidListDTO);
        doThrow(new RuntimeException("Database error")).when(bidListRepository)
                                                       .save(any(BidList.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> bidListService.addBidList(bidListDTO));

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la création de la bidList\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateBidList_Success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));
        bidListService.updateBidList(1, bidListDTO);
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void testUpdateBidList_Failure() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));
        doThrow(new RuntimeException("Database error")).when(bidListRepository).save(any(BidList.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bidListService.updateBidList(1, bidListDTO)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification de la bidList\"", exception.getMessage());
    }

    @Test
    void testDeleteBidList_Success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));
        bidListService.deleteBidList(1);
        verify(bidListRepository, times(1)).delete(any(BidList.class));
    }

    @Test
    void testDeleteBidList_Failure() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList1));
        doThrow(new RuntimeException("Database error")).when(bidListRepository).delete(any(BidList.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bidListService.deleteBidList(1)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la suppression de la bidList\"", exception.getMessage());
    }
}
