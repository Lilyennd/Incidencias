package cl.GestionDrones.v1.incidencias.mapper;

import cl.GestionDrones.v1.incidencias.dto.CreateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.dto.UpdateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.model.Incidencia;

public class IncidenciaMapper {

    /**
     * Convierte CreateIncidenciaRequest a la entidad Incidencias (para POST).
     * El ID se pasa como null ya que Hibernate e Identity lo autogenerarán en la BD.
     */
    public static Incidencia toModel(CreateIncidenciaRequest request) {
        return new Incidencia(
                null, // ID autogenerado por la base de datos
                request.idPlanVuelo(),
                request.origenReporte(),
                request.tipoIncidencia(),
                request.descripcion(),
                request.fechaHoraReporte(),
                request.ubicacionReferencial()
        );
    }

    /**
     * Convierte UpdateIncidenciaRequest a la entidad Incidencias (para PUT).
     * El ID se obtiene directamente del path parameter de la URL del endpoint.
     */
    public static Incidencia toModel(Long id, UpdateIncidenciaRequest request) {
        return new Incidencia(
                id, // ID proveniente del @PathVariable de la URL
                request.idPlanVuelo(),
                request.origenReporte(),
                request.tipoIncidencia(),
                request.descripcion(),
                request.fechaHoraReporte(),
                request.ubicacionReferencial()
        );
    }
}