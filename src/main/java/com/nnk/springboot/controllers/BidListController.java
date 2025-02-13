package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDTO;
import com.nnk.springboot.services.BidListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BidListController {
    @Autowired
    private BidListService bidListService;

    @RequestMapping("/bidList/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("bidList/list");
        mav.addObject("bidLists", bidListService.getBidLists());
        return mav;
    }

    @GetMapping("/bidList/add")
    public ModelAndView addBidForm() {
        ModelAndView mav = new ModelAndView("bidList/add");
        mav.addObject("bidList", new BidListDTO());
        return mav;
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDTO bidListDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.addBidList(bidListDTO);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("bidList/update");
        mav.addObject("bidList", bidListService.findBidListToUpdate(id));
        return mav;
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bidList") BidListDTO bidListDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.updateBidList(id, bidListDTO);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBidList(id);
        return "redirect:/bidList/list";
    }
}
