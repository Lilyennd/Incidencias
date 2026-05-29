package cl.GestionDrones.v1.incidencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    
    @Query(value = "SELECT * FROM incidencias WHERE id_plan_vuelo = :idPlanVuelo", nativeQuery = true)
    List<Incidencia> selectPorIdPlanVuelo(@Param("idPlanVuelo") Long idPlanVuelo);

    
    @Query(value = "SELECT * FROM incidencias WHERE origen_reporte = :origenReporte", nativeQuery = true)
    List<Incidencia> selectPorOrigenReporte(@Param("origenReporte") String origenReporte);

    default int totalIncidencias() {
        return (int) this.count(); 
    }
}
