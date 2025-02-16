package com.nnk.springboot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

/**
 * ExceptionHandlerController provides a centralized mechanism for handling
 * {@link ResponseStatusException} exceptions thrown within the application.
 */
@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * Handles {@link ResponseStatusException}.
     *
     * @param ex the {@link ResponseStatusException} that was thrown
     * @return a {@link ModelAndView} object configured with error details and the appropriate HTTP status
     */
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResponseStatusException(ResponseStatusException ex) {
        ModelAndView mav = new ModelAndView("access/error");

        HttpStatus status = (HttpStatus) ex.getStatusCode();
        mav.setStatus(status);

        mav.addObject("title", status.value() + " " + status.getReasonPhrase());
        mav.addObject("message", ex.getReason());

        logger.error("Handling ResponseStatusException: {} - {}", status, ex.getReason());
        return mav;
    }
}
