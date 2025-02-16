package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDTO;
import com.nnk.springboot.repositories.BidListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for managing bid list entries.
 */
@Service
public class BidListService {
    private static final Logger logger = LoggerFactory.getLogger(BidListService.class);
    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Retrieves all bid lists.
     *
     * @return a list of {@link BidListDTO} containing bid list entries.
     */
    public List<BidListDTO> getBidLists() {
        logger.info("Fetching all bid lists.");

        return bidListRepository.findAll()
                                .stream()
                                .map(bidList -> {
                                    BidListDTO bidListDTO = new BidListDTO();
                                    bidListDTO.setId(bidList.getId());
                                    bidListDTO.setAccount(bidList.getAccount());
                                    bidListDTO.setType(bidList.getType());
                                    bidListDTO.setBidQuantity(bidList.getBidQuantity());
                                    return bidListDTO;
                                })
                                .toList();
    }

    /**
     * Retrieves a specific bid list by its ID.
     *
     * @param id the ID of the bid list to retrieve.
     * @return the {@link BidList} corresponding to the given ID.
     * @throws ResponseStatusException if the bid list is not found.
     */
    public BidList getBidList(Integer id) {
        logger.info("Fetching bid list with ID: {}", id);

        return bidListRepository.findById(id)
                                .orElseThrow(() -> {
                                    logger.warn("BidList with ID {} not found.", id);
                                    return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                            "La bidList avec l'id %d n'existe pas", id));
                                });
    }

    /**
     * Prepares a bid list for updating.
     *
     * @param id the ID of the bid list to update.
     * @return the {@link BidListDTO} containing the bid list details for updating.
     * @throws ResponseStatusException if the bid list is not found.
     */
    public BidListDTO findBidListToUpdate(Integer id) {
        logger.info("Finding bid list to update with ID: {}", id);

        BidList bidList = getBidList(id);
        BidListDTO bidListDTO = new BidListDTO();
        bidListDTO.setId(bidList.getId());
        bidListDTO.setAccount(bidList.getAccount());
        bidListDTO.setType(bidList.getType());
        bidListDTO.setBidQuantity(bidList.getBidQuantity());
        return bidListDTO;
    }

    /**
     * Adds a new bid list entry.
     *
     * @param bidListAddDTO the {@link BidListDTO} containing details of the bid list to add.
     * @throws ResponseStatusException if an error occurs during the creation of the bid list.
     */
    public void addBidList(BidListDTO bidListAddDTO) {
        logger.info("Adding new bid list: {}", bidListAddDTO);

        BidList bidList = new BidList();
        bidList.setAccount(bidListAddDTO.getAccount());
        bidList.setType(bidListAddDTO.getType());
        bidList.setBidQuantity(bidListAddDTO.getBidQuantity());

        try {
            bidListRepository.save(bidList);
            logger.info("Bid list added successfully with ID: {}", bidList.getId());
        } catch (Exception e) {
            logger.error("An error occurred while creating the bid list: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la création de la bidList");
        }
    }

    /**
     * Updates an existing bid list entry.
     *
     * @param id the ID of the bid list to update.
     * @param bidListDTO the {@link BidListDTO} containing updated details of the bid list.
     * @throws ResponseStatusException if an error occurs during the update of the bid list.
     */
    public void updateBidList(Integer id, BidListDTO bidListDTO) {
        logger.info("Updating bid list with ID: {}", id);

        BidList bidList = getBidList(id);
        bidList.setAccount(bidListDTO.getAccount());
        bidList.setType(bidListDTO.getType());
        bidList.setBidQuantity(bidListDTO.getBidQuantity());

        try {
            bidListRepository.save(bidList);
            logger.info("Bid list updated successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while updating the bid list with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification de la bidList");
        }
    }

    /**
     * Deletes a bid list entry.
     *
     * @param id the ID of the bid list to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the bid list.
     */
    public void deleteBidList(Integer id) {
        logger.info("Deleting bid list with ID: {}", id);

        BidList bidList = getBidList(id);

        try {
            bidListRepository.delete(bidList);
            logger.info("Bid list deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting the bid list with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression de la bidList");
        }
    }
}
