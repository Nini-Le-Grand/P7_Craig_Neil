package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RegisterDTO;
import com.nnk.springboot.services.AccessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccessController {
    @Autowired
    private AccessService accessService;

    @GetMapping("register")
    public ModelAndView getRegistrationPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("registerDTO", new RegisterDTO());
        mav.setViewName("access/register");
        return mav;
    }

    @PostMapping("register")
    public ModelAndView register(@ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO, BindingResult result) {
        ModelAndView mav = new ModelAndView("/access/register");
        accessService.register(registerDTO, result);
        if (!result.hasErrors()) {
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @GetMapping("login")
    public ModelAndView login() {
        return new ModelAndView("access/login");
    }

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:/bidList/list");
    }
}