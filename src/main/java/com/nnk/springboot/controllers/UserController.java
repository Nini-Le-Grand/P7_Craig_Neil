package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("user/list");
        mav.addObject("users", userService.getUsers());
        return mav;
    }

    @GetMapping("/user/add")
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("user/add");
        mav.addObject("user", new UserDTO());
        return mav;
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        userService.addUser(userDTO, result);
        if (result.hasErrors()) {
            return "user/add";
        }
        return "redirect:/admin/user/list";
    }

    @GetMapping("/user/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("user/update");
        mav.addObject("user", userService.findUserToUpdate(id));
        return mav;
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        userService.updateUser(id, userDTO, result);
        if (result.hasErrors()) {
            return "user/update";
        }
        return "redirect:/admin/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/user/list";
    }
}
