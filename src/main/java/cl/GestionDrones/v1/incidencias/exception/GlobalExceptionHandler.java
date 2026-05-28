package cl.GestionDrones.v1.incidencias.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler modernizado con Problem Details API (RFC 7807)
 * Personalizado para el módulo de Gestión de Incidencias - DGAC (2026)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler DE INCIDENCIAS SE HA REGISTRADO CORRECTAMENTE");
    }

    /**
     * Maneja errores de validación Jakarta con Problem Details (Ej: campos obligatorios vacíos, descripciones muy largas)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 GlobalExceptionHandler [Incidencias] - Errores de validación detectados");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos de la incidencia"
        );

        problem.setTitle("Validation Error - Incidencia");
        problem.setProperty("timestamp", Instant.now());

        // Extraer errores con streams
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido"
                ));

        problem.setProperty("errors", errors);

        System.out.println("🔴 Errores de validación encontrados en Incidencias: " + errors);
        return problem;
    }

    /**
     * Maneja errores de parseo de JSON (Ej: Estructura mal armada al reportar la incidencia o fechas mal formateadas)
     */
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

    /**
     * Maneja casos donde una Incidencia buscada por ID no existe en la base de datos de la DGAC
     */
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

    /**
     * Maneja errores internos y caídas generales del servidor dentro del módulo de incidencias
     */
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

    /**
     * Reemplazo estratégico de 'RutInvalidoException':
     * Maneja casos donde se intenta asociar un Plan de Vuelo inexistente a la incidencia.
     */
    @ExceptionHandler(PlanVueloInvalidoException.class)
    public ProblemDetail handlePlanVueloInvalido(PlanVueloInvalidoException ex) {
        System.out.println("🟡 GlobalExceptionHandler [Incidencias] - Problema detectado con el Plan de Vuelo: " + ex.getIdPlanVuelo());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, // 404 porque el plan de vuelo indicado no se encuentra en el sistema
                ex.getMessage()
        );

        problem.setTitle("Plan de Vuelo Inválido u No Encontrado");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("idPlanVueloIngresado", ex.getIdPlanVuelo()); // Muestra qué ID causó el conflicto
        
        return problem;
    }
}