package com.cesar.sharing.config;

import com.cesar.sharing.exceptions.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@Log4j2
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    /**
     * <p>
     * This method is the main handler for all method argument errors. Generally, all error handled
     * by this method will come from the controller layer of this project.
     * @param ex {@link MethodArgumentNotValidException}}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return processValidationError(ex);
    }

    /**
     * This method is the main handler for all Business layer errors. Generally, all error handled
     * by this method will come from the service layer of this project.
     * @param ex {@See BusinessException}
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {

        Map<String, String> errors = new HashMap<>();
        String msg = ex.translate(messageSource);
        errors.put("errorMessage", msg);
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // **********************************************************************************
    // PRIVATE FUNCTIONS
    // **********************************************************************************

    /**
     * <p>
     * Builds the error message Map. Here we must consider the current configured locale, and
     * the internationalization key for each error found.
     * @param ex {{@link MethodArgumentNotValidException}}
     * @return {{@link ResponseEntity}} with all errors message
     */
    private ResponseEntity<Object> processValidationError(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        Locale locale = LocaleContextHolder.getLocale();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            FieldError fieldError = ((FieldError) error);
            String fieldName = fieldError.getField();
            String msg = messageSource.getMessage(Objects.requireNonNull(error.getDefaultMessage()), buildArgs(fieldError), locale);
            errors.put(fieldName, msg);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * <p> Builds the i18n message arguments if exists.
     * @param error The {@link FieldError} Encapsulates a field error, that is, a reason for
     * @return The array of string values to pass into the i18n message service.
     */
    private String[] buildArgs(FieldError error) {

        if ( error == null ||
                error.getArguments() == null ||
                error.getArguments().length <= 1 )
            return new String[]{};

        List<String> args = new ArrayList<>();
        for (int i = error.getArguments().length-1; i >= 1; i--) {
            Object arg = error.getArguments()[i];
            args.add(arg.toString());
        }
        return args.toArray(new String[0]);
    }

}
