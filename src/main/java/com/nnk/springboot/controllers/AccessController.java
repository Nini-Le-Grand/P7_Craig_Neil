package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RegisterDTO;
import com.nnk.springboot.services.AccessService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling access-related operations such as registration and login.
 */
@Controller
public class AccessController {
    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    @Autowired
    private AccessService accessService;

    /**
     * Displays the registration page.
     *
     * @return a {@link ModelAndView} containing the registration form view and an empty {@link RegisterDTO}
     */
    @GetMapping("register")
    public ModelAndView getRegistrationPage() {
        logger.info("Accessing registration page");

        ModelAndView mav = new ModelAndView("access/register");
        mav.addObject("registerDTO", new RegisterDTO());
        return mav;
    }

    /**
     * Handles user registration form submission.
     *
     * @param registerDTO the {@link RegisterDTO} containing user registration details
     * @param result      the {@link BindingResult} containing validation errors, if any
     * @return a {@link ModelAndView} that redirects to login on success or returns to registration page on failure
     */
    @PostMapping("register")
    public ModelAndView register(@ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO, BindingResult result) {
        logger.info("Registering user: {}", registerDTO.getUsername());

        ModelAndView mav = new ModelAndView("/access/register");
        accessService.register(registerDTO, result);

        if (!result.hasErrors()) {
            logger.info("Registration successful for user: {}", registerDTO.getUsername());
            mav.setViewName("redirect:/login");
        }

        logger.warn("Registration failed for user: {}. Errors: {}", registerDTO.getUsername(), result.getAllErrors());
        return mav;
    }

    /**
     * Displays the login page.
     *
     * @return a {@link ModelAndView} containing the login view
     */
    @GetMapping("login")
    public ModelAndView login() {
        logger.info("Accessing login page");

        return new ModelAndView("access/login");
    }

    /**
     * Redirects to the bid list page.
     *
     * @return a {@link ModelAndView} that redirects to "/bidList/list"
     */
    @GetMapping("/")
    public ModelAndView home() {
        logger.info("Redirecting to bid list page");

        return new ModelAndView("redirect:/bidList/list");
    }
}