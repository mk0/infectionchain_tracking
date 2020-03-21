/**
 *
 */
package de.chaintracker.web;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Marko Voß
 *
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> notFound(final Exception ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> badRequest(final Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<String> forbidden(final Exception ex) {
    return ResponseEntity.noContent().build();
  }
}
