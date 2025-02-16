package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDTO;
import com.nnk.springboot.services.RatingService;
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
public class RatingController {
    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);
    @Autowired
    private RatingService ratingService;

    /**
     * Displays the list of all ratings.
     *
     * @return a {@link ModelAndView} object containing the view name "rating/list" and the list of ratings.
     */
    @RequestMapping("/rating/list")
    public ModelAndView home() {
        logger.info("Displaying the list of ratings");

        ModelAndView mav = new ModelAndView("rating/list");
        mav.addObject("ratings", ratingService.getRatings());
        return mav;
    }

    /**
     * Displays the form for adding a new rating.
     *
     * @return a {@link ModelAndView} object containing the view name "rating/add" and an empty {@link RatingDTO}.
     */
    @GetMapping("/rating/add")
    public ModelAndView addRatingForm() {
        logger.info("Displaying form to add a new rating");

        ModelAndView mav = new ModelAndView("rating/add");
        mav.addObject("rating", new RatingDTO());
        return mav;
    }

    /**
     * Validates and processes the form submission for adding a new rating.
     *
     * @param ratingDTO the {@link RatingDTO} containing form data for the new rating; validated before processing.
     * @param result    the {@link BindingResult} holding validation errors, if any.
     * @return the view name "rating/add" if there are validation errors; otherwise, a redirect to {@code /rating/list}.
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDTO ratingDTO, BindingResult result) {
        logger.info("Adding rating: {}", ratingDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a rating: {}", result.getAllErrors());
            return "rating/add";
        }

        ratingService.addRating(ratingDTO);

        logger.info("redirecting to rating list");
        return "redirect:/rating/list";
    }

    /**
     * Displays the form for updating an existing rating.
     *
     * @param id the unique identifier of the rating to update.
     * @return a {@link ModelAndView} object containing the view name "rating/update" and the rating data to edit.
     */
    @GetMapping("/rating/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Displaying form to update rating with ID: {}", id);

        ModelAndView mav = new ModelAndView("rating/update");
        mav.addObject("rating", ratingService.findRatingToUpdate(id));
        return mav;
    }

    /**
     * Processes the form submission for updating an existing rating.
     *
     * @param id        the unique identifier of the rating to update.
     * @param ratingDTO the {@link RatingDTO} containing the updated rating data; validated before processing.
     * @param result    the {@link BindingResult} holding validation errors, if any.
     * @return the view name "rating/update" if there are validation errors; otherwise, a redirect to {@code /rating/list}.
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDTO ratingDTO, BindingResult result) {
        logger.info("Updating rating: {}", ratingDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating rating with ID {}: {}", id, result.getAllErrors());
            return "rating/update";
        }

        ratingService.updateRating(id, ratingDTO);

        logger.info("redirecting to rating list");
        return "redirect:/rating/list";
    }

    /**
     * Deletes an existing rating.
     *
     * @param id the unique identifier of the rating to delete.
     * @return a redirect to {@code /rating/list}.
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        logger.info("Deleting rating ID: {}", id);

        ratingService.deleteRating(id);

        logger.info("redirecting to rating list");
        return "redirect:/rating/list";
    }
}
