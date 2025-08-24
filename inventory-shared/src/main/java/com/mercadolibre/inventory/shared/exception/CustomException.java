package com.mercadolibre.inventory.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomException extends GenericException {
    private static final Logger logger = LoggerFactory.getLogger(CustomException.class);

    public CustomException(int errorCode, String message) {
        super(errorCode, message);
        logger.error("Exception CustomException: code:{}, message:{}", errorCode, message);
    }
}
