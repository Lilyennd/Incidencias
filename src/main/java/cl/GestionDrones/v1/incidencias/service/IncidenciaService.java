package cl.GestionDrones.v1.incidencias.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import cl.GestionDrones.v1.incidencias.repository.IncidenciaRepository;
import cl.GestionDrones.v1.incidencias.exception.ResourceNotFoundException;
import cl.GestionDrones.v1.incidencias.mapper.IncidenciaMapper;
import cl.GestionDrones.v1.incidencias.client.PlanesDeVuelosClient;
import cl.GestionDrones.v1.incidencias.dto.CreateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.dto.UpdateIncidenciaRequest;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private PlanesDeVuelosClient planesDeVuelosClient;

    public List<Incidencia> getIncidencias() {
        return incidenciaRepository.findAll();
    }

    public Incidencia saveIncidencia(CreateIncidenciaRequest request) {
        if (request.idPlanVuelo() != null) {
            planesDeVuelosClient.verificarPlanExiste(request.idPlanVuelo());
        }
        return incidenciaRepository.save(IncidenciaMapper.toModel(request));
    }

    public Incidencia getIncidenciaId(Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "La incidencia con ID " + id + " no está registrada en el sistema DGAC."));
    }

    public Incidencia updateIncidencia(Long id, UpdateIncidenciaRequest request) {
        if (!incidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "No se puede actualizar: La incidencia con ID " + id + " no existe.");
        }
        return incidenciaRepository.save(IncidenciaMapper.toModel(id, request));
    }

    public String deleteIncidencia(Long id) {
        if (!incidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "No se puede eliminar: La incidencia con ID " + id + " no existe.");
        }
        incidenciaRepository.deleteById(id);
        return "Incidencia eliminada";
    }

    public int totalIncidencias() {
        return (int) incidenciaRepository.count();
    }

    
    public List<Incidencia> obtenerPorPlanVuelo(Long idPlanVuelo) {
    return incidenciaRepository.selectPorIdPlanVuelo(idPlanVuelo);
    }
}