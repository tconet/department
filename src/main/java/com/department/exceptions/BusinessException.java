package com.department.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
}
