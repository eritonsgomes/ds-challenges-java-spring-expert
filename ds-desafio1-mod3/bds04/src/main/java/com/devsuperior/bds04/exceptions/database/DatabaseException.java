package com.devsuperior.bds04.exceptions.database;


public class DatabaseException extends RuntimeException {

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }

}
