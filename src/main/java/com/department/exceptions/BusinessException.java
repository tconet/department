package com.department.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessException extends RuntimeException {

    public ArrayList<String> args = new ArrayList<>();

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String arg) {
        super(message);
        this.args.add(arg);
    }

    public BusinessException(String message, ArrayList<String> args) {
        super(message);
        this.args = args;
    }

    public String translate(MessageSource messageSource) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(Objects.requireNonNull(this.getMessage()), this.args.toArray(), locale);
    }
}
