package com.berke.socialmedia.exception;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {

    private String className;
    private String details;

    public NotFoundException(String className){
        this.className = className;
    }

    public NotFoundException(String className, String details) {
        this.className = className;
        this.details = details;
    }

    @Override
    public String getMessage(){
        return className + " not found";
    }

    public String getDetails() {
        return details;
    }

}
