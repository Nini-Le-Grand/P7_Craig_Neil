package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing rating operations.
 */
@Controller
@RequestMapping("admin")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * Displays a list of all users.
     *
     * @return a {@link ModelAndView} containing the view name "user/list" and the list of users.
     */
    @RequestMapping("/user/list")
    public ModelAndView home() {
        logger.info("Displaying the list of users");

        ModelAndView mav = new ModelAndView("user/list");
        mav.addObject("users", userService.getUsers());
        return mav;
    }

    /**
     * Displays the form for adding a new user.
     *
     * @return a {@link ModelAndView} containing the view name "user/add" and an empty {@link UserDTO}.
     */
    @GetMapping("/user/add")
    public ModelAndView addUser() {
        logger.info("Displaying form to add a new user");

        ModelAndView mav = new ModelAndView("user/add");
        mav.addObject("user", new UserDTO());
        return mav;
    }

    /**
     * Validates and processes the submission of a new user.
     *
     * @param userDTO the {@link UserDTO} containing the submitted user data.
     * @param result  the {@link BindingResult} holding any validation errors.
     * @return the view name "user/add" if validation errors exist; otherwise, a redirect to {@code /admin/user/list}.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        logger.info("Adding new user: {}", userDTO.getUsername());

        userService.addUser(userDTO, result);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a user: {}", result.getAllErrors());
            return "user/add";
        }

        logger.info("redirecting to user list");
        return "redirect:/admin/user/list";
    }

    /**
     * Displays the form for updating an existing user.
     *
     * @param id the unique identifier of the user to update.
     * @return a {@link ModelAndView} containing the view name "user/update" and the user data.
     */
    @GetMapping("/user/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Displaying form to update user with ID: {}", id);

        ModelAndView mav = new ModelAndView("user/update");
        mav.addObject("user", userService.findUserToUpdate(id));
        return mav;
    }

    /**
     * Processes the update of an existing user.
     *
     * @param id      the unique identifier of the user to update.
     * @param userDTO the {@link UserDTO} containing the updated user data.
     * @param result  the {@link BindingResult} holding any validation errors.
     * @return the view name "user/update" if validation errors exist; otherwise, a redirect to {@code /admin/user/list}.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        logger.info("Updating user with ID {}: {}", id, userDTO.getUsername());

        userService.updateUser(id, userDTO, result);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating user with ID {}: {}", id, result.getAllErrors());
            return "user/update";
        }

        logger.info("redirecting to user list");
        return "redirect:/admin/user/list";
    }

    /**
     * Deletes an existing user.
     *
     * @param id the unique identifier of the user to delete.
     * @return a redirect string to {@code /admin/user/list}.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        logger.info("Deleting user with ID: {}", id);

        userService.deleteUser(id);

        logger.info("redirecting to user list");
        return "redirect:/admin/user/list";
    }
}
