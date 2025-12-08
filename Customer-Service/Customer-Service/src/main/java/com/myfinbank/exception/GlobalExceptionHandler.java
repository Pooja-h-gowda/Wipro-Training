package com.myfinbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, 
                                         HttpServletRequest request,
                                         Model model) {

       
    	
        if (request.getRequestURI().contains("/api") || 
            request.getHeader("Accept").contains("application/json")) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        
        model.addAttribute("error", ex.getMessage());
        return "error-page";  
    }

   

    @ExceptionHandler(Exception.class)
    public Object handleGeneralException(Exception ex,
                                         HttpServletRequest request,
                                         Model model) {

        ex.printStackTrace();

        if (request.getRequestURI().contains("/api") ||
            request.getHeader("Accept").contains("application/json")) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        model.addAttribute("error", ex.getMessage());
        return "error-page";  
    }
}
