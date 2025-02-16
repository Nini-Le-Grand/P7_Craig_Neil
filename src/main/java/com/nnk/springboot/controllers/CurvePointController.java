package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDTO;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing curve point operations.
 */
@Controller
public class CurvePointController {
    private static final Logger logger = LoggerFactory.getLogger(CurvePointController.class);
    @Autowired
    private CurvePointService curvePointService;

    /**
     * Displays the list of curve points.
     *
     * @return a {@link ModelAndView} containing the curve point list view and data
     */
    @RequestMapping("/curvePoint/list")
    public ModelAndView home() {
        logger.info("Accessing curve point list page");

        ModelAndView mav = new ModelAndView("curvePoint/list");
        mav.addObject("curvePoints", curvePointService.getCurvePoints());
        return mav;
    }

    /**
     * Displays the form to add a new curve point.
     *
     * @return a {@link ModelAndView} containing the add form view
     */
    @GetMapping("/curvePoint/add")
    public ModelAndView addBidForm() {
        logger.info("Accessing add curve point form");

        ModelAndView mav = new ModelAndView("curvePoint/add");
        mav.addObject("curvePoint", new CurvePointDTO());
        return mav;
    }

    /**
     * Validates and processes the addition of a new curve point.
     *
     * @param curvePointDTO the {@link CurvePointDTO} containing curve point details
     * @param result        the {@link BindingResult} containing validation errors, if any
     * @return a redirect to the curve point list page on success, or the add form on failure
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO, BindingResult result) {
        logger.info("Adding curvePoint: {}", curvePointDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a new curve point: {}", result.getAllErrors());
            return "curvePoint/add";
        }

        curvePointService.addCurvePoint(curvePointDTO);

        logger.info("redirecting to curve point list");
        return "redirect:/curvePoint/list";
    }

    /**
     * Displays the form to update an existing curve point.
     *
     * @param id the ID of the curve point to update
     * @return a {@link ModelAndView} containing the update form and curve point details
     */
    @GetMapping("/curvePoint/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Accessing update form for curve point ID: {}", id);

        ModelAndView mav = new ModelAndView("curvePoint/update");
        mav.addObject("curvePoint", curvePointService.findCurvePointToUpdate(id));
        return mav;
    }

    /**
     * Processes the update of a curve point.
     *
     * @param id           the ID of the curve point to update
     * @param curvePointDTO the updated {@link CurvePointDTO} details
     * @param result       the {@link BindingResult} containing validation errors, if any
     * @return a redirect to the curve point list page on success, or the update form on failure
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO, BindingResult result) {
        logger.info("Updating curvePoint: {}", curvePointDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating curve point ID {}: {}", id, result.getAllErrors());
            return "curvePoint/update";
        }

        curvePointService.updateCurvePoint(id, curvePointDTO);

        logger.info("redirecting to curve point list");
        return "redirect:/curvePoint/list";
    }

    /**
     * Deletes a curve point entry.
     *
     * @param id the ID of the curve point to delete
     * @return a redirect to the curve point list page
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        logger.info("Deleting curve point ID: {}", id);

        curvePointService.deleteCurvePoint(id);

        logger.info("redirecting to curve point list");
        return "redirect:/curvePoint/list";
    }
}
