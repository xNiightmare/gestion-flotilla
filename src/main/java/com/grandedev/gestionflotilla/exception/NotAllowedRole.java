package com.grandedev.gestionflotilla.exception;

public class NotAllowedRole extends RuntimeException {
    public NotAllowedRole(String message) {
        super(message);
    }
}
