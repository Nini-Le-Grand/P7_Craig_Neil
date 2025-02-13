package com.nnk.springboot.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public void handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 403) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                                  "Vous n'êtes pas autorisé à consulter cette page");
            } else if (statusCode == 404) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La page que vous cherchez n'existe pas");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");
            }
        }
    }
}
