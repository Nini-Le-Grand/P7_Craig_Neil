package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for managing trade entries.
 */
@Service
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    @Autowired
    private TradeRepository tradeRepository;

    /**
     * Retrieves all trade entries.
     *
     * @return a list of {@link TradeDTO} containing trade entries.
     */
    public List<TradeDTO> getTrades() {
        logger.info("Fetching all trades.");

        return tradeRepository.findAll()
                              .stream()
                              .map(trade -> {
                                  TradeDTO tradeDTO = new TradeDTO();
                                  tradeDTO.setId(trade.getId());
                                  tradeDTO.setAccount(trade.getAccount());
                                  tradeDTO.setType(trade.getType());
                                  tradeDTO.setBuyQuantity(trade.getBuyQuantity());
                                  return tradeDTO;
                              })
                              .toList();
    }

    /**
     * Retrieves a specific trade entry by its ID.
     *
     * @param id the ID of the trade to retrieve.
     * @return the {@link Trade} corresponding to the given ID.
     * @throws ResponseStatusException if the trade is not found.
     */
    public Trade getTrade(Integer id) {
        logger.info("Fetching trade with ID: {}", id);

        return tradeRepository.findById(id)
                              .orElseThrow(() -> {
                                  logger.warn("Trade with ID {} not found.", id);
                                  return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                          "Le trade avec l'id %d n'existe pas", id));
                              });
    }

    /**
     * Prepares a trade for updating.
     *
     * @param id the ID of the trade to update.
     * @return the {@link TradeDTO} containing the trade details for updating.
     * @throws ResponseStatusException if the trade is not found.
     */
    public TradeDTO findTradeToUpdate(Integer id) {
        logger.info("Preparing to update trade with ID: {}", id);

        Trade trade = getTrade(id);
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setId(trade.getId());
        tradeDTO.setAccount(trade.getAccount());
        tradeDTO.setType(trade.getType());
        tradeDTO.setBuyQuantity(trade.getBuyQuantity());
        return tradeDTO;
    }

    /**
     * Adds a new trade entry.
     *
     * @param tradeDTO the {@link TradeDTO} containing details of the trade to add.
     * @throws ResponseStatusException if an error occurs during the creation of the trade.
     */
    public void addTrade(TradeDTO tradeDTO) {
        logger.info("Adding new trade: {}", tradeDTO);

        Trade trade = new Trade();
        trade.setAccount(tradeDTO.getAccount());
        trade.setType(tradeDTO.getType());
        trade.setBuyQuantity(tradeDTO.getBuyQuantity());

        try {
            tradeRepository.save(trade);
            logger.info("Trade added successfully with ID: {}", trade.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding trade: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du trade");
        }
    }

    /**
     * Updates an existing trade entry.
     *
     * @param id the ID of the trade to update.
     * @param tradeDTO the {@link TradeDTO} containing updated details of the trade.
     * @throws ResponseStatusException if an error occurs during the update of the trade.
     */
    public void updateTrade(Integer id, TradeDTO tradeDTO) {
        logger.info("Updating trade with ID: {}", id);

        Trade trade = getTrade(id);
        trade.setAccount(tradeDTO.getAccount());
        trade.setType(tradeDTO.getType());
        trade.setBuyQuantity(tradeDTO.getBuyQuantity());

        try {
            tradeRepository.save(trade);
            logger.info("Trade updated successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while updating trade with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du trade");
        }
    }

    /**
     * Deletes a trade entry.
     *
     * @param id the ID of the trade to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the trade.
     */
    public void deleteTrade(Integer id) {
        logger.info("Deleting trade with ID: {}", id);

        Trade trade = getTrade(id);

        try {
            tradeRepository.delete(trade);
            logger.info("Trade deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting trade with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du trade");
        }
    }
}
