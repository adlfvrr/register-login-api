package adlf.register_login.api.exception;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioExistenteException(UsuarioExistenteException ex){
        Map<String, Object> exception = new HashMap<>();
        exception.put("timestamp", LocalDateTime.now());
        exception.put("status", HttpStatus.BAD_REQUEST.value());
        exception.put("error", "Usuario ya existe");
        exception.put("message", ex.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
