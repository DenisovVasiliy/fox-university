package com.foxminded.foxuniversity.controllers.handlers;

import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.exceptions.QueryRestrictedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AppExceptionHandler {

    private static final String ERROR_PAGE = "error-page";

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ModelAndView illegalArgument(RuntimeException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("warnMessage", "Page not found. Please, check the link.");
        modelAndView.setViewName(ERROR_PAGE);
        return modelAndView;
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ModelAndView notFound(EntityNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("warnMessage", ex.getMessage());
        modelAndView.setViewName(ERROR_PAGE);
        return modelAndView;
    }

    @ExceptionHandler(value = {QueryNotExecuteException.class})
    public ModelAndView notFound(QueryNotExecuteException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Sorry, something went wrong...");
        modelAndView.setViewName(ERROR_PAGE);
        return modelAndView;
    }

    @ExceptionHandler(value = {QueryRestrictedException.class})
    public ModelAndView restricted(QueryRestrictedException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.setViewName(ERROR_PAGE);
        return modelAndView;
    }
}
