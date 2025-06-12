package com.socialmedia.socialmedia.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super(email);
    }
}
