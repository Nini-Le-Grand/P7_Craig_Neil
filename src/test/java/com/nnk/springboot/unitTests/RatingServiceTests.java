package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDTO;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
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
public class RatingServiceTests {
    @Mock
    private RatingRepository ratingRepository;
    @InjectMocks
    private RatingService ratingService;
    private Rating rating1;
    private Rating rating2;
    private RatingDTO ratingDTO;

    @BeforeEach
    void setUp() {
        rating1 = new Rating();
        rating1.setId(1);
        rating1.setFitchRating("1");

        rating2 = new Rating();
        rating2.setId(2);
        rating2.setFitchRating("2");

        ratingDTO = new RatingDTO();
        ratingDTO.setFitchRating("3");
    }

    @Test
    void testGetRatings() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating1, rating2));

        List<RatingDTO> result = ratingService.getRatings();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0)
                                 .getFitchRating());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testGetRating_Success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        Rating result = ratingService.getRating(1);

        assertNotNull(result);
        assertEquals("1", result.getFitchRating());
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    void testGetRating_Failure() {
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> ratingService.findRatingToUpdate(1));

        assertEquals("404 NOT_FOUND \"Le rating avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testFindRatingToUpdate_Success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));

        RatingDTO result = ratingService.findRatingToUpdate(1);

        assertNotNull(result);
        assertEquals("1", result.getFitchRating());
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    void testAddRating_Success() {
        ratingService.addRating(ratingDTO);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testAddRating_Failure() {
        ratingService.addRating(ratingDTO);
        doThrow(new RuntimeException("Database error")).when(ratingRepository)
                                                       .save(any(Rating.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> ratingService.addRating(ratingDTO));

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la creation du rating\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateRating_Success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        ratingService.updateRating(1, ratingDTO);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testUpdateRating_Failure() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        doThrow(new RuntimeException("Database error")).when(ratingRepository).save(any(Rating.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ratingService.updateRating(1, ratingDTO)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification du rating\"", exception.getMessage());
    }

    @Test
    void testDeleteRating_Success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        ratingService.deleteRating(1);
        verify(ratingRepository, times(1)).delete(any(Rating.class));
    }

    @Test
    void testDeleteRating_Failure() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        doThrow(new RuntimeException("Database error")).when(ratingRepository).delete(any(Rating.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ratingService.deleteRating(1)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la suppression du rating\"", exception.getMessage());
    }
}
