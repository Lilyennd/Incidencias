package cl.GestionDrones.v1.incidencias.mapper;

import cl.GestionDrones.v1.incidencias.dto.CreateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.dto.UpdateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.model.Incidencia;

public class IncidenciaMapper {

    
    public static Incidencia toModel(CreateIncidenciaRequest request) {
        return new Incidencia(
                null,
                request.idPlanVuelo(),
                request.origenReporte(),
                request.tipoIncidencia(),
                request.descripcion(),
                request.fechaHoraReporte(),
                request.ubicacionReferencial()
        );
    }

    
    public static Incidencia toModel(Long id, UpdateIncidenciaRequest request) {
        return new Incidencia(
                id,
                request.idPlanVuelo(),
                request.origenReporte(),
                request.tipoIncidencia(),
                request.descripcion(),
                request.fechaHoraReporte(),
                request.ubicacionReferencial()
        );
    }
}