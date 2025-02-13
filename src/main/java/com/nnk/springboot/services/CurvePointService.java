package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePointDTO> getCurvePoints() {
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

    private CurvePoint getCurvePoint(Integer id) {
        return curvePointRepository.findById(id)
                                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                           "Le curvePoint avec l'id %d n'existe pas", id)));
    }

    public CurvePointDTO findCurvePointToUpdate(Integer id) {
        CurvePoint curvePoint = getCurvePoint(id);
        CurvePointDTO curvePointDTO = new CurvePointDTO();
        curvePointDTO.setId(curvePoint.getId());
        curvePointDTO.setTerm(curvePoint.getTerm());
        curvePointDTO.setValue(curvePoint.getValue());
        return curvePointDTO;
    }

    public void addCurvePoint(CurvePointDTO curvePointDTO) {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(curvePointDTO.getTerm());
        curvePoint.setValue(curvePointDTO.getValue());

        try {
            curvePointRepository.save(curvePoint);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la création du curvePoint");
        }
    }

    public void updateCurvePoint(Integer id, CurvePointDTO curvePointDTO) {
        CurvePoint curvePoint = getCurvePoint(id);
        curvePoint.setTerm(curvePointDTO.getTerm());
        curvePoint.setValue(curvePointDTO.getValue());

        try {
            curvePointRepository.save(curvePoint);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification du curvePoint");
        }
    }

    public void deleteCurvePoint(Integer id) {
        CurvePoint curvePoint = getCurvePoint(id);

        try {
            curvePointRepository.delete(curvePoint);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression du curvePoint");
        }
    }
}
