package com.karty.kartyjavatest.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * The type Api error.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    /**
     * Instantiates a new Api error.
     *
     * @param status  the status
     * @param message the message
     */
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
