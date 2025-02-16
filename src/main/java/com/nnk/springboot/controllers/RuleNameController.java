package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDTO;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing rule name operations.
 */
@Controller
public class RuleNameController {
    private static final Logger logger = LoggerFactory.getLogger(RuleNameController.class);
    @Autowired
    private RuleNameService ruleNameService;

    /**
     * Displays a list of all rule names.
     *
     * @return a {@link ModelAndView} object containing the view name "ruleName/list" and the list of rule names.
     */
    @RequestMapping("/ruleName/list")
    public ModelAndView home() {
        logger.info("Displaying the list of rule names");

        ModelAndView mav = new ModelAndView("ruleName/list");
        mav.addObject("ruleNames", ruleNameService.getRuleNames());
        return mav;
    }

    /**
     * Displays the form for creating a new rule name.
     *
     * @return a {@link ModelAndView} object containing the view name "ruleName/add" and an empty {@link RuleNameDTO}.
     */
    @GetMapping("/ruleName/add")
    public ModelAndView addRuleNameForm() {
        logger.info("Displaying form to add a new rule name");

        ModelAndView mav = new ModelAndView("ruleName/add");
        mav.addObject("ruleName", new RuleNameDTO());
        return mav;
    }

    /**
     * Validates and processes the form submission for creating a new rule name.
     *
     * @param ruleNameDTO the {@link RuleNameDTO} containing the submitted rule name data.
     * @param result      the {@link BindingResult} holding any validation errors.
     * @return the view name "ruleName/add" if validation fails; otherwise, a redirect to {@code /ruleName/list}.
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO, BindingResult result) {
        logger.info("Adding rule name: {}", ruleNameDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a rule name: {}", result.getAllErrors());
            return "ruleName/add";
        }

        ruleNameService.addRuleName(ruleNameDTO);

        logger.info("redirecting to rule name list");
        return "redirect:/ruleName/list";
    }

    /**
     * Displays the form for updating an existing rule name.
     *
     * @param id the unique identifier of the rule name to update.
     * @return a {@link ModelAndView} object containing the view name "ruleName/update" and the rule name data.
     */
    @GetMapping("/ruleName/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Displaying form to update rule name with ID: {}", id);

        ModelAndView mav = new ModelAndView("ruleName/update");
        mav.addObject("ruleName", ruleNameService.findRuleNameToUpdate(id));
        return mav;
    }

    /**
     * Processes the update of an existing rule name.
     *
     * @param id          the unique identifier of the rule name to update.
     * @param ruleNameDTO the {@link RuleNameDTO} containing the updated rule name data.
     * @param result      the {@link BindingResult} holding any validation errors.
     * @return the view name "ruleName/update" if validation fails; otherwise, a redirect to {@code /ruleName/list}.
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO, BindingResult result) {
        logger.info("Updating rule name with ID {}: {}", id, ruleNameDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating rule name with ID {}: {}", id,
                        result.getAllErrors());
            return "ruleName/update";
        }

        ruleNameService.updateRuleName(id, ruleNameDTO);

        logger.info("redirecting to rule name list");
        return "redirect:/ruleName/list";
    }

    /**
     * Deletes an existing rule name.
     *
     * @param id the unique identifier of the rule name to delete.
     * @return a redirect string to {@code /ruleName/list}.
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        logger.info("Deleting rule name with ID: {}", id);

        ruleNameService.deleteRuleName(id);

        logger.info("redirecting to rule name list");
        return "redirect:/ruleName/list";
    }
}
