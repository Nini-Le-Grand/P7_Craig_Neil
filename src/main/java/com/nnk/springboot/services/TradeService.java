package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    public List<TradeDTO> getTrades() {
        List<Trade> trades = tradeRepository.findAll();
        return trades.stream().map(trade -> {
            TradeDTO tradeDTO = new TradeDTO();
            tradeDTO.setId(trade.getTradeId());
            tradeDTO.setType(trade.getType());
            tradeDTO.setAccount(trade.getAccount());
            tradeDTO.setBuyQuantity(trade.getBuyQuantity());
            return tradeDTO;
        }).toList();
    }
}
