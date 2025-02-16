package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for managing curve point entries.
 */
@Service
public class CurvePointService {
    private static final Logger logger = LoggerFactory.getLogger(CurvePointService.class);
    @Autowired
    private CurvePointRepository curvePointRepository;

    /**
     * Retrieves all curve points.
     *
     * @return a list of {@link CurvePointDTO} containing curve point entries.
     */
    public List<CurvePointDTO> getCurvePoints() {
        logger.info("Fetching all curve points.");

        return curvePointRepository.findAll()
                                   .stream()
                                   .map(curvePoint -> {
                                       CurvePointDTO curvePointDTO = new CurvePointDTO();
                                       curvePointDTO.setId(curvePoint.getId());
                                       curvePointDTO.setCurveId(curvePoint.getCurveId());
                                       curvePointDTO.setTerm(curvePoint.getTerm());
                                       curvePointDTO.setValue(curvePoint.getValue());
                                       return curvePointDTO;
                                   })
                                   .toList();
    }

    /**
     * Retrieves a specific curve point by its ID.
     *
     * @param id the ID of the curve point to retrieve.
     * @return the {@link CurvePoint} corresponding to the given ID.
     * @throws ResponseStatusException if the curve point is not found.
     */
    public CurvePoint getCurvePoint(Integer id) {
        logger.info("Fetching curve point with ID: {}", id);

        return curvePointRepository.findById(id)
                                   .orElseThrow(() -> {
                                       logger.warn("CurvePoint with ID {} not found.", id);
                                       return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                               "Le curvePoint avec l'id %d n'existe pas", id));
                                   });
    }

    /**
     * Prepares a curve point for updating.
     *
     * @param id the ID of the curve point to update.
     * @return the {@link CurvePointDTO} containing the curve point details for updating.
     * @throws ResponseStatusException if the curve point is not found.
     */
    public CurvePointDTO findCurvePointToUpdate(Integer id) {
        logger.info("Preparing to update curve point with ID: {}", id);

        CurvePoint curvePoint = getCurvePoint(id);
        CurvePointDTO curvePointDTO = new CurvePointDTO();
        curvePointDTO.setId(curvePoint.getId());
        curvePointDTO.setTerm(curvePoint.getTerm());
        curvePointDTO.setValue(curvePoint.getValue());
        return curvePointDTO;
    }

    /**
     * Adds a new curve point entry.
     *
     * @param curvePointDTO the {@link CurvePointDTO} containing details of the curve point to add.
     * @throws ResponseStatusException if an error occurs during the creation of the curve point.
     */
    public void addCurvePoint(CurvePointDTO curvePointDTO) {
        logger.info("Adding new curve point: {}", curvePointDTO);

        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(curvePointDTO.getTerm());
        curvePoint.setValue(curvePointDTO.getValue());

        try {
            curvePointRepository.save(curvePoint);
            logger.info("Curve point added successfully with ID: {}", curvePoint.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding curve point: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la création du curvePoint");
        }
    }

    /**
     * Updates an existing curve point entry.
     *
     * @param id            the ID of the curve point to update.
     * @param curvePointDTO the {@link CurvePointDTO} containing updated details of the curve point.
     * @throws ResponseStatusException if an error occurs during the update of the curve point.
     */
    public void updateCurvePoint(Integer id, CurvePointDTO curvePointDTO) {
        logger.info("Updating curve point with ID: {}", id);

        CurvePoint curvePoint = getCurvePoint(id);
        curvePoint.setTerm(curvePointDTO.getTerm());
        curvePoint.setValue(curvePointDTO.getValue());

        try {
            curvePointRepository.save(curvePoint);
            logger.info("Curve point updated successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while updating curve point with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du curvePoint");
        }
    }

    /**
     * Deletes a curve point entry.
     *
     * @param id the ID of the curve point to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the curve point.
     */
    public void deleteCurvePoint(Integer id) {
        logger.info("Deleting curve point with ID: {}", id);

        CurvePoint curvePoint = getCurvePoint(id);

        try {
            curvePointRepository.delete(curvePoint);
            logger.info("Curve point deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting curve point with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du curvePoint");
        }
    }
}
