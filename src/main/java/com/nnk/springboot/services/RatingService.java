package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public List<RatingDTO> getRatings() {
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

    public Rating getRating(Integer id) {
        return ratingRepository.findById(id)
                               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                       "Le rating avec l'id %d n'existe pas", id)));
    }

    public RatingDTO findRatingToUpdate(Integer id) {
        Rating rating = getRating(id);
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setMoodysRating(rating.getMoodysRating());
        ratingDTO.setSandPRating(rating.getSandPRating());
        ratingDTO.setFitchRating(rating.getFitchRating());
        ratingDTO.setOrder(rating.getOrderNumber());
        return ratingDTO;
    }

    public void addRating(RatingDTO ratingDTO) {
        Rating rating = new Rating();
        rating.setMoodysRating(ratingDTO.getMoodysRating());
        rating.setSandPRating(ratingDTO.getSandPRating());
        rating.setFitchRating(ratingDTO.getFitchRating());
        rating.setOrderNumber(ratingDTO.getOrder());

        try {
            ratingRepository.save(rating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation du rating");
        }
    }

    public void updateRating(Integer id, RatingDTO ratingDTO) {
        Rating rating = getRating(id);
        rating.setMoodysRating(ratingDTO.getMoodysRating());
        rating.setSandPRating(ratingDTO.getSandPRating());
        rating.setFitchRating(ratingDTO.getFitchRating());
        rating.setOrderNumber(ratingDTO.getOrder());

        try {
            ratingRepository.save(rating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du rating");
        }
    }

    public void deleteRating(Integer id) {
        Rating rating = getRating(id);

        try {
            ratingRepository.delete(rating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du rating");
        }
    }
}
