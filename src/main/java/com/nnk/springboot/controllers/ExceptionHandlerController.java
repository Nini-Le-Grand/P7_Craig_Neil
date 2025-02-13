package com.nnk.springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResponseStatusException(ResponseStatusException ex) {
        ModelAndView mav = new ModelAndView("access/error");

        HttpStatus status = (HttpStatus) ex.getStatusCode();
        mav.setStatus(status);

        mav.addObject("title", status.value() + " " + status.getReasonPhrase());
        mav.addObject("message", ex.getReason());
        return mav;
    }
}
