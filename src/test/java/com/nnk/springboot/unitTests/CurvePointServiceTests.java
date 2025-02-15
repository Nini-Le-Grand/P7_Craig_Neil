package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDTO;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
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
public class CurvePointServiceTests {
    @Mock
    private CurvePointRepository curvePointRepository;
    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint1;
    private CurvePoint curvePoint2;
    private CurvePointDTO curvePointDTO;

    @BeforeEach
    void setUp() {
        curvePoint1 = new CurvePoint();
        curvePoint1.setId(1);
        curvePoint1.setValue(10.0);
        curvePoint1.setTerm(10.0);

        curvePoint2 = new CurvePoint();
        curvePoint2.setId(2);
        curvePoint2.setValue(20.0);
        curvePoint2.setTerm(20.0);

        curvePointDTO = new CurvePointDTO();
        curvePointDTO.setValue(30.0);
        curvePointDTO.setTerm(30.0);
    }

    @Test
    void testGetCurvePoints() {
        when(curvePointRepository.findAll()).thenReturn(List.of(curvePoint1, curvePoint2));

        List<CurvePointDTO> result = curvePointService.getCurvePoints();

        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0)
                                          .getValue());
        verify(curvePointRepository, times(1)).findAll();
    }

    @Test
    void testGetCurvePoint_Success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        CurvePoint result = curvePointService.getCurvePoint(1);

        assertNotNull(result);
        assertEquals(10.0, result.getValue());
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    void testGetCurvePoint_Failure() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> curvePointService.findCurvePointToUpdate(1));

        assertEquals("404 NOT_FOUND \"Le curvePoint avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testFindCurvePointToUpdate_Success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));

        CurvePointDTO result = curvePointService.findCurvePointToUpdate(1);

        assertNotNull(result);
        assertEquals(10.0, result.getValue());
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    void testAddCurvePoint_Success() {
        curvePointService.addCurvePoint(curvePointDTO);
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
    }

    @Test
    void testAddCurvePoint_Failure() {
        curvePointService.addCurvePoint(curvePointDTO);
        doThrow(new RuntimeException("Database error")).when(curvePointRepository)
                                                       .save(any(CurvePoint.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> curvePointService.addCurvePoint(curvePointDTO));

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la création du curvePoint\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateCurvePoint_Success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        curvePointService.updateCurvePoint(1, curvePointDTO);
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
    }

    @Test
    void testUpdateCurvePoint_Failure() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        doThrow(new RuntimeException("Database error")).when(curvePointRepository).save(any(CurvePoint.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                curvePointService.updateCurvePoint(1, curvePointDTO)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification du curvePoint\"", exception.getMessage());
    }

    @Test
    void testDeleteCurvePoint_Success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        curvePointService.deleteCurvePoint(1);
        verify(curvePointRepository, times(1)).delete(any(CurvePoint.class));
    }

    @Test
    void testDeleteCurvePoint_Failure() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        doThrow(new RuntimeException("Database error")).when(curvePointRepository).delete(any(CurvePoint.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                curvePointService.deleteCurvePoint(1)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la suppression du curvePoint\"", exception.getMessage());
    }
}
