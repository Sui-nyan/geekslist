package hdm.stuttgart.geekslist.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GeeksListErrorController implements ErrorController {
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleBadEnumConversion(ConversionFailedException ex) {
        return new ResponseEntity<>(
                String.format("Incorrect value '%s' for Argument '%s'", ex.getValue(), ex.getTargetType()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleBadRequestException(ResponseStatusException ex) {
        HttpStatusCode statusCode = ex.getStatusCode();
        if (statusCode.equals(HttpStatus.NOT_FOUND)) {
            return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } else if (statusCode.equals(HttpStatus.BAD_REQUEST)) {
            return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } if (statusCode.equals(HttpStatus.TOO_MANY_REQUESTS)) {
            return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS);
        }

        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

    private ResponseEntity<String> handleExceptionInternal(RuntimeException ex, HttpHeaders httpHeaders, HttpStatusCode httpStatus) {
        ResponseEntity<String> response = new ResponseEntity<>(ex.getMessage(), httpHeaders, httpStatus);
        return response;
    }
}
