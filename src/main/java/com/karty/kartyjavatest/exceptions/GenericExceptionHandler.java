package com.karty.kartyjavatest.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Generic exception handler.
 */
@ControllerAdvice
public class GenericExceptionHandler extends BaseExceptionHandler {

    /**
     * Instantiates a new Generic exception handler.
     */
    public GenericExceptionHandler() {
        super();
    }

    /**
     * Instantiates a new Generic exception handler.
     *
     * @param messageSource the message source
     */
    @Autowired
    public GenericExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handle generic exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleGenericException(final Exception ex) {

        logger.error(
                String.format("Uncaught error. Stacktrace is : %s", ex + getFullStackTraceLog(ex)));
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                getTranslatedMessage("error.generic.internal.server.error", null));
        return buildResponseEntity(apiError);
    }

    private String getFullStackTraceLog(Exception ex) {
        return Arrays.stream(ex.getStackTrace()).map(Objects::toString)
                .collect(Collectors.joining());
    }

}
