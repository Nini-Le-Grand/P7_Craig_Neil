package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    public List<TradeDTO> getTrades() {
        return tradeRepository.findAll()
                              .stream()
                              .map(trade -> {
                                  TradeDTO tradeDTO = new TradeDTO();
                                  tradeDTO.setId(trade.getTradeId());
                                  tradeDTO.setAccount(trade.getAccount());
                                  tradeDTO.setType(trade.getType());
                                  tradeDTO.setBuyQuantity(trade.getBuyQuantity());
                                  return tradeDTO;
                              })
                              .toList();
    }

    private Trade getTrade(Integer id) {
        return tradeRepository.findById(id)
                              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                      "Le trade avec l'id %d n'existe pas", id)));
    }

    public TradeDTO findTradeToUpdate(Integer id) {
        Trade trade = getTrade(id);
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setId(trade.getTradeId());
        tradeDTO.setAccount(trade.getAccount());
        tradeDTO.setType(trade.getType());
        tradeDTO.setBuyQuantity(trade.getBuyQuantity());
        return tradeDTO;
    }

    public void addTrade(TradeDTO tradeDTO) {
        Trade trade = new Trade();
        trade.setAccount(tradeDTO.getAccount());
        trade.setType(tradeDTO.getType());
        trade.setBuyQuantity(tradeDTO.getBuyQuantity());

        try {
            tradeRepository.save(trade);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du trade");
        }
    }

    public void updateTrade(Integer id, TradeDTO tradeDTO) {
        Trade trade = getTrade(id);
        trade.setAccount(tradeDTO.getAccount());
        trade.setType(tradeDTO.getType());
        trade.setBuyQuantity(tradeDTO.getBuyQuantity());

        try {
            tradeRepository.save(trade);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du trade");
        }
    }

    public void deleteTrade(Integer id) {
        Trade trade = getTrade(id);

        try {
            tradeRepository.delete(trade);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du trade");
        }
    }
}
