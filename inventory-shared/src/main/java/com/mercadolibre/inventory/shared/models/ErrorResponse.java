package com.mercadolibre.inventory.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for standardizing error response format in API responses.
 * This class is used to create a uniform structure for error messages, making it easier for clients
 * to parse and understand error details.
 *
 * <p>It includes fields for status (typically false in error scenarios), a code representing the error,
 * a message providing more details about the error, and an optional data field for any additional information.</p>
 *
 * <p>Annotations like {@link JsonNaming} and {@link JsonIgnoreProperties} are used to ensure
 * proper serialization and deserialization of JSON data, adhering to API conventions such as snake_case naming.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private int codeStatus;
    private String message;
}
