package com.mercadolibre.inventory.shared.exception;

/**
 * Abstract base class for custom exceptions in the application.
 * This class extends {@link RuntimeException} and adds an additional field for an error code,
 * allowing for a more structured error handling approach within the application.
 *
 * <p>The error code provides a convenient way to categorize and identify the nature of the error,
 * facilitating better error reporting and handling mechanisms.</p>
 *
 * <p>Derived classes can utilize this class to create more specific exceptions with distinct error codes
 * and messages, tailored to various error scenarios encountered in the application.</p>
 */
public abstract class GenericException extends RuntimeException {

    private final int errorCode;

    /**
     * Constructs a new GenericException with the specified detail message, error code, and cause.
     *
     * @param errorCode The error code associated with this exception.
     * @param message The detailed message for this exception.
     * @param cause The cause of this exception (a null value is permitted, and indicates that the cause is nonexistent or unknown).
     */
    public GenericException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new GenericException with the specified detail message and error code.
     *
     * @param errorCode The error code associated with this exception.
     * @param message The detailed message for this exception.
     */
    public GenericException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return The error code.
     */
    public int getErrorCode() {
        return errorCode;
    }
}
