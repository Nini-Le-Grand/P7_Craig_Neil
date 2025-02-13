package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDTO;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("ruleName/list");
        mav.addObject("ruleNames", ruleNameService.getRuleNames());
        return mav;
    }

    @GetMapping("/ruleName/add")
    public ModelAndView addRuleNameForm() {
        ModelAndView mav = new ModelAndView("ruleName/add");
        mav.addObject("ruleName", new RuleNameDTO());
        return mav;
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.addRuleName(ruleNameDTO);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("ruleName/update");
        mav.addObject("ruleName", ruleNameService.findRuleNameToUpdate(id));
        return mav;
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleNameService.updateRuleName(id, ruleNameDTO);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteRuleName(id);
        return "redirect:/ruleName/list";
    }
}
