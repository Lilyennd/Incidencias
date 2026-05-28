package cl.GestionDrones.v1.incidencias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO para actualizar una incidencia existente (PUT).
 * No incluye ID porque se obtiene del path parameter en la URL del endpoint.
 * Mantiene la flexibilidad de 'idPlanVuelo' para casos reportados por ciudadanos.
 */
public record UpdateIncidenciaRequest(

        // Se mantiene opcional por si la incidencia original no tenía un plan de vuelo asociado
        Long idPlanVuelo,

        @NotBlank(message = "El origen del reporte es obligatorio (Ej: Piloto, Empresa Mandante, Ciudadano)")
        String origenReporte,

        @NotBlank(message = "El tipo de incidencia es obligatorio")
        String tipoIncidencia,

        @NotBlank(message = "La descripción de la incidencia es obligatoria")
        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "La fecha y hora del reporte es obligatoria")
        LocalDateTime fechaHoraReporte,

        @NotBlank(message = "La ubicación referencial es obligatoria para la fiscalización de la DGAC")
        String ubicacionReferencial
) {}