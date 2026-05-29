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

    
    public int totalIncidencias() {
        return (int) incidenciaRepository.count();
    }

    
    public int totalIncidenciasV2() {
        return incidenciaRepository.totalIncidencias();
    }

    
    public List<Incidencia> obtenerPorPlanVuelo(Long idPlanVuelo) {
        
        List<Incidencia> listaResultados = java.util.Collections.emptyList();

        if (listaResultados.isEmpty()) {
            throw new PlanVueloInvalidoException(idPlanVuelo);
        }

        return listaResultados;
    }
}