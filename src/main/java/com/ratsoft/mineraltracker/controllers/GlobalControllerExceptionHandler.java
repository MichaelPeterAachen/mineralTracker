package com.ratsoft.mineraltracker.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
    /**
     * Exception handling for a data violation
     *
     * @param req the request handler causing the problem.
     * @param ex  the exception being caught.
     * @return a model and view.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @NonNull ModelAndView handleDataViolationError(final HttpServletRequest req, final @NonNull Exception ex) {
        log.error("Request: {} raised a data violation: {}", req.getRequestURL(), ex.getMessage());

        final ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/error");

        return mav;
    }

    /**
     * Exception handling for a general exceptions.
     *
     * @param req the request handler causing the problem.
     * @param ex  the exception being caught.
     * @return a model and view.
     */
    @ExceptionHandler(Exception.class)
    public @NonNull ModelAndView handleError(final HttpServletRequest req, final @NonNull Exception ex) {
        log.error("Request: {} raised: {}", req.getRequestURL(), ex.getMessage());

        final ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/error");

        return mav;
    }

}
