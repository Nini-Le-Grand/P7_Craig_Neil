package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.*;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleNameDTO> getRuleNames() {
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

    private RuleName getRuleName(Integer id) {
        return ruleNameRepository.findById(id)
                                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                         "Le ruleName avec l'id %d n'existe pas", id)));
    }

    public RuleNameDTO findRuleNameToUpdate(Integer id) {
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

    public void addRuleName(RuleNameDTO ruleNameDTO) {
        RuleName ruleName = new RuleName();
        ruleName.setName(ruleNameDTO.getName());
        ruleName.setDescription(ruleNameDTO.getDescription());
        ruleName.setJson(ruleNameDTO.getJson());
        ruleName.setTemplate(ruleNameDTO.getTemplate());
        ruleName.setSqlStr(ruleNameDTO.getSql());
        ruleName.setSqlPart(ruleNameDTO.getSqlPart());

        try {
            ruleNameRepository.save(ruleName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la creation de la ruleName");
        }
    }

    public void updateRuleName(Integer id, RuleNameDTO ruleNameDTO) {
        RuleName ruleName = getRuleName(id);
        ruleName.setName(ruleNameDTO.getName());
        ruleName.setDescription(ruleNameDTO.getDescription());
        ruleName.setJson(ruleNameDTO.getJson());
        ruleName.setTemplate(ruleNameDTO.getTemplate());
        ruleName.setSqlStr(ruleNameDTO.getSql());
        ruleName.setSqlPart(ruleNameDTO.getSqlPart());

        try {
            ruleNameRepository.save(ruleName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification de la ruleName");
        }
    }

    public void deleteRuleName(Integer id) {
        RuleName ruleName = getRuleName(id);

        try {
            ruleNameRepository.delete(ruleName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression de la ruleName");
        }
    }
}
