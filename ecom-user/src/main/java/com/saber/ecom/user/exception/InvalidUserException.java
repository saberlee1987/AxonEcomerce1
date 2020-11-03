package com.saber.ecom.user.exception;

public class InvalidUserException extends RuntimeException {
    private final String user;
    public InvalidUserException(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    @Override
    public String getMessage() {
        return "InvalidUser =====>" + user;
    }
}
