package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDTO;
import com.nnk.springboot.services.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("trade/list");
        mav.addObject("trades", tradeService.getTrades());
        return mav;
    }

    @GetMapping("/trade/add")
    public ModelAndView addTradeForm() {
        ModelAndView mav = new ModelAndView("trade/add");
        mav.addObject("trade", new TradeDTO());
        return mav;
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.addTrade(tradeDTO);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("trade/update");
        mav.addObject("trade", tradeService.findTradeToUpdate(id));
        return mav;
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        tradeService.updateTrade(id, tradeDTO);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteTrade(id);
        return "redirect:/trade/list";
    }
}
