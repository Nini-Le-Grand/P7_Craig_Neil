package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDTO;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CurvePointController {
    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("curvePoint/list");
        mav.addObject("curvePoints", curvePointService.getCurvePoints());
        return mav;
    }

    @GetMapping("/curvePoint/add")
    public ModelAndView addBidForm() {
        ModelAndView mav = new ModelAndView("curvePoint/add");
        mav.addObject("curvePoint", new CurvePointDTO());
        return mav;
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.addCurvePoint(curvePointDTO);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("curvePoint/update");
        mav.addObject("curvePoint", curvePointService.findCurvePointToUpdate(id));
        return mav;
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(id, curvePointDTO);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePoint(id);
        return "redirect:/curvePoint/list";
    }
}
