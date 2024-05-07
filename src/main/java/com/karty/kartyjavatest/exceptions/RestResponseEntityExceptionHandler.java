package com.karty.kartyjavatest.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.net.MalformedURLException;
import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Rest response entity exception handler.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseEntityExceptionHandler extends BaseExceptionHandler {

    /**
     * Instantiates a new Rest response entity exception handler.
     */
    public RestResponseEntityExceptionHandler() {
        super();
    }

    /**
     * Instantiates a new Rest response entity exception handler.
     *
     * @param messageSource the message source
     */
    @Autowired
    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (ObjectError error : ex.getAllErrors()) {

            String errorMessage = error.getDefaultMessage() != null
                    ? getTranslatedMessage(error.getDefaultMessage(), null) :
                    getTranslatedMessage(error);
            String fullErrorMsg = error.getObjectName() + ":" + errorMessage;

            logger.warn(fullErrorMsg);
            errors.add(fullErrorMsg);
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                getTranslatedMessage("error.validation", null));
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {


        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String code = error.getCode();
            Object[] args = error.getArguments();
            String errorMessage =
                    code != null ? getTranslatedMessage(code, args) : error.getDefaultMessage();
            errors.add(error.getField() + ":" + errorMessage);


        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {

            String errorMessage = error.getDefaultMessage() != null
                    ? getTranslatedMessage(error.getDefaultMessage(), null) :
                    getTranslatedMessage(error);
            errors.add(errorMessage);


        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                getTranslatedMessage(ex.getLocalizedMessage(), null));
        apiError.setErrors(errors);
        logger.error(ExceptionUtils.getStackTrace(ex));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle access denied exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex) {

        String errorMessage = "error.access.denied";

        if (!errorMessage.equals(ex.getMessage())
                && !ex.getMessage().equals(getTranslatedMessage(errorMessage, null))) {
            errorMessage = ex.getMessage();
        }
        logger.error(ExceptionUtils.getStackTrace(ex));
        ApiError apiError =
                new ApiError(HttpStatus.FORBIDDEN, getTranslatedMessage(errorMessage, null));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle authentication exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(final AuthenticationException ex) {

        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.UNAUTHORIZED, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle NotFoundException.
     *
     * @param ex NotFoundException object
     * @return ResponseEntity response entity
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.resource-not-found");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(final UsernameNotFoundException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.user-not-found");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(final BadCredentialsException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.bad-credentials");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({AuthTokenExpiredException.class})
    public ResponseEntity<Object> handleAuthTokenExpiredException(final AuthTokenExpiredException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.auth-token-expired");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.data-validation");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExistsException(final AlreadyExistsException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        logger.trace(ExceptionUtils.getStackTrace(ex));

        List<String> errors = new ArrayList<>();
        errors.add("error.resource-already-exists");

        String translatedMessage = getTranslatedMessage(ex.getMessage(), ex.getStackTrace());
        ApiError error = new ApiError(HttpStatus.CONFLICT, translatedMessage, errors);
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle NotFoundException.
     *
     * @param ex NotModifiedException object
     * @return ResponseEntity response entity
     */
    @ExceptionHandler({NotModifiedException.class})
    public ResponseEntity<Object> handleNotModifiedException(final NotModifiedException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error = new ApiError(HttpStatus.NOT_MODIFIED, getTranslatedMessage(ex.getMessage(), ex.getStackTrace()));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle NotFoundException.
     *
     * @param ex NotFoundException object
     * @return ResponseEntity response entity
     */
    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<Object> handleInternalServerException(final InternalServerException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle MalformedURLException.
     *
     * @param ex MalformedURLException object
     * @return ResponseEntity Object
     */
    @ExceptionHandler({MalformedURLException.class})
    public ResponseEntity<Object> handleMalformedUrlException(final MalformedURLException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.BAD_REQUEST, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle IllegalArgumentException.
     *
     * @param ex IllegalArgumentException object
     * @return ResponseEntity Object
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, getTranslatedMessage(ex.getMessage(), ex.getStackTrace()));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle JSONException.
     *
     * @param ex JSONException object
     * @return ResponseEntity Object
     */
    @ExceptionHandler({JSONException.class})
    public ResponseEntity<Object> handleJsonException(final JSONException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.BAD_REQUEST, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());
    }

    /**
     * Handle ConstraintViolationException.
     *
     * @param ex      ConstraintViolationException object
     * @param request the request
     * @return ResponseEntity Object
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> constraintViolationException(Exception ex, WebRequest request) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.BAD_REQUEST, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());

    }

    /**
     * Handle DateTimeParseException.
     *
     * @param ex DateTimeParseException object
     * @return ResponseEntity Object
     */
    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<Object> handleDateTimeParseException(
            final DateTimeParseException ex) {
        logger.error(ExceptionUtils.getMessage(ex));
        ApiError error =
                new ApiError(HttpStatus.BAD_REQUEST, getTranslatedMessage(ex.getMessage(), null));
        return new ResponseEntity<>(error, error.getStatus());
    }
}
