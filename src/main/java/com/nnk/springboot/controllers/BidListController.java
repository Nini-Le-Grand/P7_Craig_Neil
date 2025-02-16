package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDTO;
import com.nnk.springboot.services.BidListService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing bid list operations.
 */
@Controller
public class BidListController {
    private static final Logger logger = LoggerFactory.getLogger(BidListController.class);
    @Autowired
    private BidListService bidListService;

    /**
     * Displays the list of bid entries.
     *
     * @return a {@link ModelAndView} containing the bid list view and bid list data
     */
    @RequestMapping("/bidList/list")
    public ModelAndView home() {
        logger.info("Accessing bid list page");

        ModelAndView mav = new ModelAndView("bidList/list");
        mav.addObject("bidLists", bidListService.getBidLists());
        return mav;
    }

    /**
     * Displays the form to add a new bid.
     *
     * @return a {@link ModelAndView} containing the bid form view
     */
    @GetMapping("/bidList/add")
    public ModelAndView addBidForm() {
        logger.info("Accessing add bid form");

        ModelAndView mav = new ModelAndView("bidList/add");
        mav.addObject("bidList", new BidListDTO());
        return mav;
    }

    /**
     * Validates and processes the addition of a new bid.
     *
     * @param bidListDTO the {@link BidListDTO} containing bid details
     * @param result     the {@link BindingResult} containing validation errors, if any
     * @return a redirect to the bid list page on success, or the add form on failure
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDTO bidListDTO, BindingResult result) {
        logger.info("Adding bidList: {}", bidListDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a new bid: {}", result.getAllErrors());
            return "bidList/add";
        }

        bidListService.addBidList(bidListDTO);

        logger.info("redirecting to bid list");
        return "redirect:/bidList/list";
    }

    /**
     * Displays the form to update an existing bid.
     *
     * @param id the ID of the bid to update
     * @return a {@link ModelAndView} containing the update form and bid details
     */
    @GetMapping("/bidList/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Accessing update form for bid ID: {}", id);

        ModelAndView mav = new ModelAndView("bidList/update");
        mav.addObject("bidList", bidListService.findBidListToUpdate(id));
        return mav;
    }

    /**
     * Processes the update of a bid.
     *
     * @param id         the ID of the bid to update
     * @param bidListDTO the updated {@link BidListDTO} details
     * @param result     the {@link BindingResult} containing validation errors, if any
     * @return a redirect to the bid list page on success, or the update form on failure
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bidList") BidListDTO bidListDTO, BindingResult result) {
        logger.info("Updating bidList: {}", id);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating bid ID {}: {}", id, result.getAllErrors());
            return "bidList/update";
        }

        bidListService.updateBidList(id, bidListDTO);

        logger.info("redirecting to bid list");
        return "redirect:/bidList/list";
    }

    /**
     * Deletes a bid entry.
     *
     * @param id the ID of the bid to delete
     * @return a redirect to the bid list page
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        logger.info("Deleting bid ID: {}", id);

        bidListService.deleteBidList(id);

        logger.info("redirecting to bid list");
        return "redirect:/bidList/list";
    }
}
