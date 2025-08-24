package com.mercadolibre.inventory.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponse<T> {

    @Schema(description = "The HTTP status code of the response.")
    private int codeStatus;

    @Schema(description = "A message providing more details about the response.")
    private String message;

    @Schema(description = "The data payload of the response, which can be any type of object.")
    private T data;

    public static <T> GenericResponse of(int code,String message,T data){
       return GenericResponse.builder()
               .codeStatus(code)
               .message(message)
               .data(data)
               .build();
    }

}
