package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service class for managing rule name entries.
 */
@Service
public class RuleNameService {
    private static final Logger logger = LoggerFactory.getLogger(RuleNameService.class);
    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * Retrieves all rule names.
     *
     * @return a list of {@link RuleNameDTO} containing rule name entries.
     */
    public List<RuleNameDTO> getRuleNames() {
        logger.info("Fetching all rule names.");

        return ruleNameRepository.findAll()
                                 .stream()
                                 .map(ruleName -> {
                                     RuleNameDTO ruleNameDTO = new RuleNameDTO();
                                     ruleNameDTO.setId(ruleName.getId());
                                     ruleNameDTO.setName(ruleName.getName());
                                     ruleNameDTO.setDescription(ruleName.getDescription());
                                     ruleNameDTO.setJson(ruleName.getJson());
                                     ruleNameDTO.setTemplate(ruleName.getTemplate());
                                     ruleNameDTO.setSql(ruleName.getSqlStr());
                                     ruleNameDTO.setSqlPart(ruleName.getSqlPart());
                                     return ruleNameDTO;
                                 })
                                 .toList();
    }

    /**
     * Retrieves a specific rule name by its ID.
     *
     * @param id the ID of the rule name to retrieve.
     * @return the {@link RuleName} corresponding to the given ID.
     * @throws ResponseStatusException if the rule name is not found.
     */
    public RuleName getRuleName(Integer id) {
        logger.info("Fetching rule name with ID: {}", id);

        return ruleNameRepository.findById(id)
                                 .orElseThrow(() -> {
                                     logger.warn("Rule name with ID {} not found.", id);
                                     return new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                             "Le ruleName avec l'id %d n'existe pas", id));
                                 });
    }

    /**
     * Prepares a rule name for updating.
     *
     * @param id the ID of the rule name to update.
     * @return the {@link RuleNameDTO} containing the rule name details for updating.
     * @throws ResponseStatusException if the rule name is not found.
     */
    public RuleNameDTO findRuleNameToUpdate(Integer id) {
        logger.info("Preparing to update rule name with ID: {}", id);

        RuleName ruleName = getRuleName(id);
        RuleNameDTO ruleNameDTO = new RuleNameDTO();
        ruleNameDTO.setId(ruleName.getId());
        ruleNameDTO.setName(ruleName.getName());
        ruleNameDTO.setDescription(ruleName.getDescription());
        ruleNameDTO.setJson(ruleName.getJson());
        ruleNameDTO.setTemplate(ruleName.getTemplate());
        ruleNameDTO.setSql(ruleName.getSqlStr());
        ruleNameDTO.setSqlPart(ruleName.getSqlPart());
        return ruleNameDTO;
    }

    /**
     * Adds a new rule name entry.
     *
     * @param ruleNameDTO the {@link RuleNameDTO} containing details of the rule name to add.
     * @throws ResponseStatusException if an error occurs during the creation of the rule name.
     */
    public void addRuleName(RuleNameDTO ruleNameDTO) {
        logger.info("Adding new rule name: {}", ruleNameDTO);

        RuleName ruleName = new RuleName();
        ruleName.setName(ruleNameDTO.getName());
        ruleName.setDescription(ruleNameDTO.getDescription());
        ruleName.setJson(ruleNameDTO.getJson());
        ruleName.setTemplate(ruleNameDTO.getTemplate());
        ruleName.setSqlStr(ruleNameDTO.getSql());
        ruleName.setSqlPart(ruleNameDTO.getSqlPart());

        try {
            ruleNameRepository.save(ruleName);
            logger.info("Rule name added successfully with ID: {}", ruleName.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding rule name: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation de la ruleName");
        }
    }

    /**
     * Updates an existing rule name entry.
     *
     * @param id the ID of the rule name to update.
     * @param ruleNameDTO the {@link RuleNameDTO} containing updated details of the rule name.
     * @throws ResponseStatusException if an error occurs during the update of the rule name.
     */
    public void updateRuleName(Integer id, RuleNameDTO ruleNameDTO) {
        logger.info("Updating rule name with ID: {}", id);

        RuleName ruleName = getRuleName(id);
        ruleName.setName(ruleNameDTO.getName());
        ruleName.setDescription(ruleNameDTO.getDescription());
        ruleName.setJson(ruleNameDTO.getJson());
        ruleName.setTemplate(ruleNameDTO.getTemplate());
        ruleName.setSqlStr(ruleNameDTO.getSql());
        ruleName.setSqlPart(ruleNameDTO.getSqlPart());

        try {
            ruleNameRepository.save(ruleName);
            logger.info("Rule name updated successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while updating rule name with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification de la ruleName");
        }
    }

    /**
     * Deletes a rule name entry.
     *
     * @param id the ID of the rule name to delete.
     * @throws ResponseStatusException if an error occurs during the deletion of the rule name.
     */
    public void deleteRuleName(Integer id) {
        logger.info("Deleting rule name with ID: {}", id);

        RuleName ruleName = getRuleName(id);

        try {
            ruleNameRepository.delete(ruleName);
            logger.info("Rule name deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting rule name with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression de la ruleName");
        }
    }
}
