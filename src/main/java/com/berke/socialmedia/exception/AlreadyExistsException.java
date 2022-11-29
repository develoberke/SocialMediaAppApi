package com.berke.socialmedia.exception;

public class AlreadyExistsException extends RuntimeException{

    private String className;
    private String details;

    public AlreadyExistsException(String className){
        this.className = className;
    }

    public AlreadyExistsException(String className, String details) {
        this.className = className;
        this.details = details;
    }

    @Override
    public String getMessage(){
        return className + " is already exists";
    }

    public String getDetails() {
        return details;
    }
}
