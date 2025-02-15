package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
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
public class TradeServiceTests {
    @Mock
    private TradeRepository tradeRepository;
    @InjectMocks
    private TradeService tradeService;
    private Trade trade1;
    private Trade trade2;
    private TradeDTO tradeDTO;

    @BeforeEach
    void setUp() {
        trade1 = new Trade();
        trade1.setId(1);
        trade1.setAccount("1");

        trade2 = new Trade();
        trade2.setId(2);
        trade2.setAccount("2");

        tradeDTO = new TradeDTO();
        tradeDTO.setAccount("3");
    }

    @Test
    void testGetTrades() {
        when(tradeRepository.findAll()).thenReturn(List.of(trade1, trade2));

        List<TradeDTO> result = tradeService.getTrades();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0)
                                .getAccount());
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    void testGetTrade_Success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        Trade result = tradeService.getTrade(1);

        assertNotNull(result);
        assertEquals("1", result.getAccount());
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    void testGetTrade_Failure() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> tradeService.findTradeToUpdate(1));

        assertEquals("404 NOT_FOUND \"Le trade avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testFindTradeToUpdate_Success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));

        TradeDTO result = tradeService.findTradeToUpdate(1);

        assertNotNull(result);
        assertEquals("1", result.getAccount());
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    void testAddTrade_Success() {
        tradeService.addTrade(tradeDTO);
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void testAddTrade_Failure() {
        tradeService.addTrade(tradeDTO);
        doThrow(new RuntimeException("Database error")).when(tradeRepository)
                                                       .save(any(Trade.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> tradeService.addTrade(tradeDTO));

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la creation du trade\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateTrade_Success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        tradeService.updateTrade(1, tradeDTO);
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void testUpdateTrade_Failure() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        doThrow(new RuntimeException("Database error")).when(tradeRepository).save(any(Trade.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                tradeService.updateTrade(1, tradeDTO)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification du trade\"", exception.getMessage());
    }

    @Test
    void testDeleteTrade_Success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        tradeService.deleteTrade(1);
        verify(tradeRepository, times(1)).delete(any(Trade.class));
    }

    @Test
    void testDeleteTrade_Failure() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        doThrow(new RuntimeException("Database error")).when(tradeRepository).delete(any(Trade.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                tradeService.deleteTrade(1)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la suppression du trade\"", exception.getMessage());
    }
}
