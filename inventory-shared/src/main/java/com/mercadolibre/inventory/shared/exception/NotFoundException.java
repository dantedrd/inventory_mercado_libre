package com.mercadolibre.inventory.shared.exception;

public class NotFoundException extends GenericException{
    public NotFoundException(int errorCode, String message) {
        super(errorCode, message);
    }
}
