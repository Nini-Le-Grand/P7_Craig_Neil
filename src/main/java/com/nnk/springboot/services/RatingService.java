package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for managing rating entries.
 */
@Service
public class RatingService {
    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);
    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Retrieves all ratings.
     *
     * @return a list of {@link RatingDTO} containing rating entries.
     */
    public List<RatingDTO> getRatings() {
        logger.info("Fetching all ratings.");

        return ratingRepository.findAll()
                               .stream()
                               .map(rating -> {
                                   RatingDTO ratingDTO = new RatingDTO();
                                   ratingDTO.setId(rating.getId());
                                   ratingDTO.setMoodysRating(rating.getMoodysRating());
                                   ratingDTO.setSandPRating(rating.getSandPRating());
                                   ratingDTO.setFitchRating(rating.getFitchRating());
                                   ratingDTO.setOrder(rating.getOrderNumber());
                                   return ratingDTO;
                               })
                               .toList();
    }

    /**
     * Retrieves a specific rating by its ID.
     *
     * @param id the ID of the rating to retrieve.
     * @return the {@link Rating} corresponding to the given ID.
     * @throws ResponseStatusException if the rating is not found.
     */
    public Rating getRating(Integer id) {
        logger.info("Fetching rating with ID: {}", id);

        return ratingRepository.findById(id)
                               .orElseThrow(() -> {
                                   logger.warn("Rating with ID {} not found.", id);
                                   return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                           "Le rating avec l'id %d n'existe pas", id));
                               });
    }

    /**
     * Prepares a rating for updating.
     *
     * @param id the ID of the rating to update.
     * @return the {@link RatingDTO} containing the rating details for updating.
     * @throws ResponseStatusException if the rating is not found.
     */
    public RatingDTO findRatingToUpdate(Integer id) {
        logger.info("Preparing to update rating with ID: {}", id);

        Rating rating = getRating(id);
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setMoodysRating(rating.getMoodysRating());
        ratingDTO.setSandPRating(rating.getSandPRating());
        ratingDTO.setFitchRating(rating.getFitchRating());
        ratingDTO.setOrder(rating.getOrderNumber());
        return ratingDTO;
    }

    /**
     * Adds a new rating entry.
     *
     * @param ratingDTO the {@link RatingDTO} containing details of the rating to add.
     * @throws ResponseStatusException if an error occurs during the creation of the rating.
     */
    public void addRating(RatingDTO ratingDTO) {
        logger.info("Adding new rating: {}", ratingDTO);

        Rating rating = new Rating();
        rating.setMoodysRating(ratingDTO.getMoodysRating());
        rating.setSandPRating(ratingDTO.getSandPRating());
        rating.setFitchRating(ratingDTO.getFitchRating());
        rating.setOrderNumber(ratingDTO.getOrder());

        try {
            ratingRepository.save(rating);
            logger.info("Rating added successfully with ID: {}", rating.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding rating: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du rating");
        }
    }

    /**
     * Updates an existing rating entry.
     *
     * @param id the ID of the rating to update.
     * @param ratingDTO the {@link RatingDTO} containing updated details of the rating.
     * @throws ResponseStatusException if an error occurs during the update of the rating.
     */
    public void updateRating(Integer id, RatingDTO ratingDTO) {
        logger.info("Updating rating with ID: {}", id);

        Rating rating = getRating(id);
        rating.setMoodysRating(ratingDTO.getMoodysRating());
        rating.setSandPRating(ratingDTO.getSandPRating());
        rating.setFitchRating(ratingDTO.getFitchRating());
        rating.setOrderNumber(ratingDTO.getOrder());

        try {
            ratingRepository.save(rating);
            logger.info("Rating updated successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while updating rating with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du rating");
        }
    }

    /**
     * Deletes a rating entry.
     *
     * @param id the ID of the rating to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the rating.
     */
    public void deleteRating(Integer id) {
        logger.info("Deleting rating with ID: {}", id);

        Rating rating = getRating(id);

        try {
            ratingRepository.delete(rating);
            logger.info("Rating deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting rating with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du rating");
        }
    }
}
