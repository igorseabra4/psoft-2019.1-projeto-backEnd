package psoft.projeto.exception;

import java.util.Date;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import psoft.projeto.model.CustomRestError;

@ControllerAdvice
public class RestExceptionHandler {

   @ExceptionHandler(Exception.class)
   public ResponseEntity<CustomRestError> handleAnyException(Exception ex, WebRequest request) {
       CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage(), request.getDescription(false));
       return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler({UserNotFoundException.class, })
   public ResponseEntity<CustomRestError> notFound(Exception ex, WebRequest request) {
       CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage(), request.getDescription(false));
       return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler({UserAlreadyExistsException.class, })
   public ResponseEntity<CustomRestError> alreadyExists(Exception ex, WebRequest request) {
       CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage(), request.getDescription(false));
       return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
   }

   @ExceptionHandler({IllegalArgumentException.class, })
   public ResponseEntity<CustomRestError> invalidFields(Exception ex, WebRequest request) {
       CustomRestError errorMessage = new CustomRestError(new Date(), ex.getMessage(), request.getDescription(false));
       return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
   }
}
