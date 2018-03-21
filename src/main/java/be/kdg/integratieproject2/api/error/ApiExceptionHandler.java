package be.kdg.integratieproject2.api.error;

import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    static Logger log = Logger.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
        errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        log.warn("Invalid request: " + request.toString());
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request){
        log.warn("Invalid request: " + request.toString());
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    protected ResponseEntity<Object> handleUserNotAuthorizedException(UserNotAuthorizedException ex, WebRequest request)
    {
        log.warn("Invalid request: " + request.toString());
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
        return new ResponseEntity(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request)
    {
        log.warn("Invalid request: " + request.toString());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity(apiError, apiError.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<Object> handleInvalidTokenException(UserAlreadyExistsException ex, WebRequest request)
    {
        log.warn("Invalid request: " + request.toString());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity(apiError, apiError.getStatus());
    }
}
