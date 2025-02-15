package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDTO;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameServiceTests {
    @Mock
    private RuleNameRepository ruleNameRepository;
    @InjectMocks
    private RuleNameService ruleNameService;
    private RuleName ruleName1;
    private RuleName ruleName2;
    private RuleNameDTO ruleNameDTO;

    @BeforeEach
    void setUp() {
        ruleName1 = new RuleName();
        ruleName1.setId(1);
        ruleName1.setName("Test1");

        ruleName2 = new RuleName();
        ruleName2.setId(2);
        ruleName2.setName("Test2");

        ruleNameDTO = new RuleNameDTO();
        ruleNameDTO.setName("Test3");
    }

    @Test
    void testGetRuleNames() {
        when(ruleNameRepository.findAll()).thenReturn(List.of(ruleName1, ruleName2));

        List<RuleNameDTO> result = ruleNameService.getRuleNames();

        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0)
                                .getName());
        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    void testGetRuleName_Success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        RuleName result = ruleNameService.getRuleName(1);

        assertNotNull(result);
        assertEquals("Test1", result.getName());
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    void testGetRuleName_Failure() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> ruleNameService.findRuleNameToUpdate(1));

        assertEquals("404 NOT_FOUND \"Le ruleName avec l'id 1 n'existe pas\"", exception.getMessage());
    }

    @Test
    void testFindRuleNameToUpdate_Success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));

        RuleNameDTO result = ruleNameService.findRuleNameToUpdate(1);

        assertNotNull(result);
        assertEquals("Test1", result.getName());
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    void testAddRuleName_Success() {
        ruleNameService.addRuleName(ruleNameDTO);
        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
    }

    @Test
    void testAddRuleName_Failure() {
        ruleNameService.addRuleName(ruleNameDTO);
        doThrow(new RuntimeException("Database error")).when(ruleNameRepository)
                                                       .save(any(RuleName.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> ruleNameService.addRuleName(ruleNameDTO));

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la creation de la ruleName\"",
                     exception.getMessage());
    }

    @Test
    void testUpdateRuleName_Success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        ruleNameService.updateRuleName(1, ruleNameDTO);
        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
    }

    @Test
    void testUpdateRuleName_Failure() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        doThrow(new RuntimeException("Database error")).when(ruleNameRepository).save(any(RuleName.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ruleNameService.updateRuleName(1, ruleNameDTO)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la modification de la ruleName\"", exception.getMessage());
    }

    @Test
    void testDeleteRuleName_Success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        ruleNameService.deleteRuleName(1);
        verify(ruleNameRepository, times(1)).delete(any(RuleName.class));
    }

    @Test
    void testDeleteRuleName_Failure() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        doThrow(new RuntimeException("Database error")).when(ruleNameRepository).delete(any(RuleName.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ruleNameService.deleteRuleName(1)
        );

        assertEquals("500 INTERNAL_SERVER_ERROR \"Une erreur est survenue lors de la suppression de la ruleName\"", exception.getMessage());
    }
}
