package userapp.brian.duran.userappapi.exception;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // Reemplazar por Result Pattern ?
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {

    Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage,
                    (msg1, msg2) -> msg1
            ));

    String dynamicDetail = String.format("La solicitud tiene %d error(es) de validación.", errors.size());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            dynamicDetail
    );

    problemDetail.setTitle("Error de Validación");

    problemDetail.setProperty("errors", errors);

    return ResponseEntity.badRequest().body(problemDetail);
  }

  // Duplicate Info -> 409
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ProblemDetail> handleDatabaseErrors(DataIntegrityViolationException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, "El recurso ya existe o viola una restricción única.");
    problemDetail.setTitle("Conflicto de Datos");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
  }

  // Illegal Arguments -> 400
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> handleBadArgs(IllegalArgumentException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGlobalErrors(Exception ex) {
    // Es buena práctica imprimir el error en consola para que tú (el dev) lo veas
    log.error("Error inesperado" + ex.getMessage());

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno inesperado.");

    problemDetail.setTitle("Error Interno del Servidor");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
  }
}
