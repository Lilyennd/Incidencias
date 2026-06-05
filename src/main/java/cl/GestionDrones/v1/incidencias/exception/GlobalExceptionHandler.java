package cl.GestionDrones.v1.incidencias.exception;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler DE INCIDENCIAS SE HA REGISTRADO CORRECTAMENTE");
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 GlobalExceptionHandler [Incidencias] - Errores de validación detectados");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos de la incidencia"
        );

        problem.setTitle("Validation Error - Incidencia");
        problem.setProperty("timestamp", Instant.now());

        
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido"
                ));

        problem.setProperty("errors", errors);

        System.out.println("🔴 Errores de validación encontrados en Incidencias: " + errors);
        return problem;
    }

    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("🟡 Error de parseo JSON en módulo Incidencias");
        System.out.println("🟡 Mensaje: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error al procesar el JSON de la incidencia enviado"
        );

        problem.setTitle("JSON Parse Error - Incidencia");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        System.out.println("🟡 Incidencia o recurso no encontrado");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );

        problem.setTitle("Incidencia Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("🔴 EXCEPCIÓN CAPTURADA EN INCIDENCIAS: " + ex.getClass().getName());
        System.out.println("🔴 Mensaje: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno en el módulo de reporte de incidencias"
        );

        problem.setTitle("Internal Server Error - Incidencias");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMessage());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        return problem;
    }

    
    @ExceptionHandler(PlanVueloInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handlePlanVueloInvalido(
        PlanVueloInvalidoException ex) {

    Map<String, Object> error = new HashMap<>();
    error.put("status", 404);
    error.put("error", "Plan de vuelo no encontrado");
    error.put("mensaje", ex.getMessage());
    error.put("timestamp", LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}