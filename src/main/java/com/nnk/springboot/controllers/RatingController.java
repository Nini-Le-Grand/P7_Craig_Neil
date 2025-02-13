package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDTO;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @RequestMapping("/rating/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("rating/list");
        mav.addObject("ratings", ratingService.getRatings());
        return mav;
    }

    @GetMapping("/rating/add")
    public ModelAndView addRatingForm() {
        ModelAndView mav = new ModelAndView("rating/add");
        mav.addObject("rating", new RatingDTO());
        return mav;
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDTO ratingDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.addRating(ratingDTO);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("rating/update");
        mav.addObject("rating", ratingService.findRatingToUpdate(id));
        return mav;
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDTO ratingDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.updateRating(id, ratingDTO);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRating(id);
        return "redirect:/rating/list";
    }
}
