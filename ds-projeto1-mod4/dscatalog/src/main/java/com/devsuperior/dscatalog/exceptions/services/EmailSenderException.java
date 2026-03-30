package com.devsuperior.dscatalog.exceptions.services;


public class EmailSenderException extends RuntimeException {

    public EmailSenderException() {
        super();
    }

    public EmailSenderException(String message) {
        super(message);
    }

}