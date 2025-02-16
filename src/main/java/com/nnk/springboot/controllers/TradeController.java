package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.services.TradeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing trade operations.
 */
@Controller
public class TradeController {
    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    @Autowired
    private TradeService tradeService;

    /**
     * Displays a list of all trades.
     *
     * @return a {@link ModelAndView} containing the view name "trade/list" and a list of trades.
     */
    @RequestMapping("/trade/list")
    public ModelAndView home() {
        logger.info("Displaying the list of trades");

        ModelAndView mav = new ModelAndView("trade/list");
        mav.addObject("trades", tradeService.getTrades());
        return mav;
    }

    /**
     * Displays the form for adding a new trade.
     *
     * @return a {@link ModelAndView} containing the view name "trade/add" and an empty {@link TradeDTO}.
     */
    @GetMapping("/trade/add")
    public ModelAndView addTradeForm() {
        logger.info("Displaying form to add a new trade");

        ModelAndView mav = new ModelAndView("trade/add");
        mav.addObject("trade", new TradeDTO());
        return mav;
    }

    /**
     * Validates and processes the submission of a new trade.
     *
     * @param tradeDTO the {@link TradeDTO} containing the submitted trade data.
     * @param result   the {@link BindingResult} that holds any validation errors.
     * @return the view name "trade/add" if validation fails; otherwise, a redirect to {@code /trade/list}.
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        logger.info("Adding trade: {}", tradeDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding a trade: {}", result.getAllErrors());
            return "trade/add";
        }

        tradeService.addTrade(tradeDTO);

        logger.info("redirecting to trade list");
        return "redirect:/trade/list";
    }

    /**
     * Displays the form for updating an existing trade.
     *
     * @param id the unique identifier of the trade to update.
     * @return a {@link ModelAndView} containing the view name "trade/update" and the trade data.
     */
    @GetMapping("/trade/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        logger.info("Displaying form to update trade with ID: {}", id);

        ModelAndView mav = new ModelAndView("trade/update");
        mav.addObject("trade", tradeService.findTradeToUpdate(id));
        return mav;
    }

    /**
     * Processes the update of an existing trade.
     *
     * @param id       the unique identifier of the trade to update.
     * @param tradeDTO the {@link TradeDTO} containing the updated trade data.
     * @param result   the {@link BindingResult} that holds any validation errors.
     * @return the view name "trade/update" if validation fails; otherwise, a redirect to {@code /trade/list}.
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        logger.info("Updating trade with ID {}: {}", id, tradeDTO);

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while updating trade with ID {}: {}", id, result.getAllErrors());
            return "trade/update";
        }

        tradeService.updateTrade(id, tradeDTO);

        logger.info("redirecting to trade list");
        return "redirect:/trade/list";
    }

    /**
     * Deletes an existing trade.
     *
     * @param id the unique identifier of the trade to delete.
     * @return a redirect string to {@code /trade/list}.
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        logger.info("Deleting trade with ID: {}", id);

        tradeService.deleteTrade(id);

        logger.info("redirecting to trade list");
        return "redirect:/trade/list";
    }
}
