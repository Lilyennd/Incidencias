package cl.GestionDrones.v1.incidencias.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import cl.GestionDrones.v1.incidencias.repository.IncidenciaRepository;
import cl.GestionDrones.v1.incidencias.exception.ResourceNotFoundException;
import cl.GestionDrones.v1.incidencias.exception.PlanVueloInvalidoException;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    public List<Incidencia> getIncidencias() {
        return incidenciaRepository.findAll();
    }

    public Incidencia saveIncidencia(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia getIncidenciaId(Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La incidencia con ID " + id + " no está registrada en el sistema DGAC."));
    }

    public Incidencia updateIncidencia(Incidencia incidencia) {
        // Primero verificamos si existe para asegurar que es un PUT válido
        if (!incidenciaRepository.existsById(incidencia.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: La incidencia con ID " + incidencia.getId() + " no existe.");
        }
        return incidenciaRepository.save(incidencia);
    }

    public String deleteIncidencia(Long id) {
        if (!incidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: La incidencia con ID " + id + " no existe.");
        }
        incidenciaRepository.deleteById(id);
        return "Incidencia eliminada";
    }

    // LA ACCIÓN LA HACE EL SERVICE (Conteo directo mediante JPA standard)
    public int totalIncidencias() {
        return (int) incidenciaRepository.count();
    }

    // LA ACCIÓN LA HACE EL REPOSITORIO (Llamando a una query personalizada nativa/JPQL si existiera)
    public int totalIncidenciasV2() {
        return incidenciaRepository.totalIncidencias();
    }

    /**
     * Métodos de búsqueda personalizados para la DGAC.
     * Permite buscar incidencias vinculadas a un plan de vuelo específico.
     * Si no se encuentra ninguna registrada, gatilla la excepción controlada por el GlobalExceptionHandler.
     */
    public List<Incidencia> obtenerPorPlanVuelo(Long idPlanVuelo) {
        // 1. Buscas en la base de datos a través del repositorio (simulación estructural)
        // List<Incidencias> listaResultados = incidenciaRepository.findByIdPlanVuelo(idPlanVuelo);
        List<Incidencia> listaResultados = java.util.Collections.emptyList(); // Simulación vacía para testeo de excepciones

        // 2. Si no hay registros asociados a ese plan de vuelo, lanzas la excepción de negocio
        if (listaResultados.isEmpty()) {
            throw new PlanVueloInvalidoException(idPlanVuelo);
        }

        return listaResultados;
    }
}