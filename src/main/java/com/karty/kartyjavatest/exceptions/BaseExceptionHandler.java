package com.karty.kartyjavatest.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * The type Base exception handler.
 */
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * The Message source.
     */
    protected MessageSource messageSource;

    /**
     * Instantiates a new Base exception handler.
     */
    public BaseExceptionHandler() {
        super();
    }

    /**
     * Build the response body in case of error.
     *
     * @param apiError ApiError object.
     * @return ResponseEntity object
     */
    protected ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Retrieve the translated exception message.
     *
     * @param ex   Exception object
     * @param args list of objects
     * @return String object
     */
    protected String getTranslatedMessage(Exception ex, Object[] args) {
        return getTranslatedMessage(ex.getMessage(), args);
    }

    /**
     * Retrieve the translated exception message.
     *
     * @param objectError ObjectError object
     * @return String object
     */
    protected String getTranslatedMessage(ObjectError objectError) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(objectError, locale);
    }

    /**
     * Retrieve the translated exception message.
     *
     * @param message String object
     * @param args    list of objects
     * @return String object
     */
    protected String getTranslatedMessage(@Nullable String message, Object[] args) {
        if (message == null) {
            return null;
        }

        Locale locale = LocaleContextHolder.getLocale();
        String[] strArgs = null;

        if (args != null) {
            strArgs = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                strArgs[i] = args[i].toString();
            }
        }

        try {
            return messageSource.getMessage(message, strArgs, locale);
        } catch (NoSuchMessageException e) {
            return message;
        }

    }
}
