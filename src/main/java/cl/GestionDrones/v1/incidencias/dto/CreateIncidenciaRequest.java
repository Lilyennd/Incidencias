package cl.GestionDrones.v1.incidencias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public record CreateIncidenciaRequest(
        
        
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