package com.mercadolibre.inventory.shared.exception;

public enum SPError {


    INVALID_PARAMETERS(1001, "Algunos parametros son invalidos"),
    INSUFFICIENT_STOCK(1002, "Se acabo el stock del item"),
    ITEM_NOT_FOUND(1002, "El valor no se ha encontrado");

    private final int errorCode;
    private final String errorMessage;

    /**
     * Constructs an SPError enum value with the specified error code and message.
     *
     * @param errorCode    The unique code associated with the error.
     * @param errorMessage The human-readable message describing the error.
     */
    SPError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Retrieves the error code of this error.
     *
     * @return The integer code representing the specific error.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Retrieves the error message of this error.
     *
     * @return The string message associated with the error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
