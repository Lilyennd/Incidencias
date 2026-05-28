package cl.GestionDrones.v1.incidencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
// 1. CORREGIDO: Cambiado de Integer a Long para coincidir con el ID del Modelo e IncidenciaService
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    // 2. CORREGIDO: Cambiado a Long y corregida la columna SQL a 'id_plan_vuelo' (por ser Query Nativa)
    @Query(value = "SELECT * FROM incidencias WHERE id_plan_vuelo = :idPlanVuelo", nativeQuery = true)
    List<Incidencia> selectPorIdPlanVuelo(@Param("idPlanVuelo") Long idPlanVuelo);

    // 3. CORREGIDO: Corregida la columna SQL a 'origen_reporte' para evitar fallos de sintaxis en BD nativa
    @Query(value = "SELECT * FROM incidencias WHERE origen_reporte = :origenReporte", nativeQuery = true)
    List<Incidencia> selectPorOrigenReporte(@Param("origenReporte") String origenReporte);

    default int totalIncidencias() {
        return (int) this.count(); 
    }
}
