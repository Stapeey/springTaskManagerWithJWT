package com.Stapi.task.Exceptions;

public class MissingFieldsException extends RuntimeException{
    public MissingFieldsException(String message){
        super(message);
    }
}
