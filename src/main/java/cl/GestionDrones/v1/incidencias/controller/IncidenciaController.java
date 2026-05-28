package cl.GestionDrones.v1.incidencias.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.incidencias.dto.CreateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.dto.UpdateIncidenciaRequest;
import cl.GestionDrones.v1.incidencias.mapper.IncidenciaMapper;
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import cl.GestionDrones.v1.incidencias.service.IncidenciaService;
import cl.GestionDrones.v1.incidencias.exception.PlanVueloInvalidoException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/incidencias")
public class IncidenciaController {

        private final IncidenciaService incidenciaService;

        // Inyección por constructor limpia y moderna
        public IncidenciaController(IncidenciaService incidenciaService) {
                this.incidenciaService = incidenciaService;
        }

        @GetMapping
        public ResponseEntity<List<Incidencia>> listarIncidencias() {
                List<Incidencia> incidencias = incidenciaService.getIncidencias();
                
                // IF: Si no hay incidencias registradas en el mapa nacional de la DGAC
                if (incidencias.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(incidencias);
        }

        @PostMapping
        public ResponseEntity<Incidencia> agregarIncidencia(@Valid @RequestBody CreateIncidenciaRequest request) {
                // IF: Validación manual de negocio. Si el plan de vuelo viene informado, no puede ser un ID absurdo
                if (request.idPlanVuelo() != null && request.idPlanVuelo() <= 0) {
                        throw new PlanVueloInvalidoException(request.idPlanVuelo(), "El ID del Plan de Vuelo asociado debe ser un identificador numérico válido.");
                }

                Incidencia nuevaIncidencia = incidenciaService.saveIncidencia(IncidenciaMapper.toModel(request));
                
                if (nuevaIncidencia == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaIncidencia);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Incidencia> buscarIncidencia(@PathVariable Long id) {
                Incidencia incidencia = incidenciaService.getIncidenciaId(id);
                
                // IF: Si no se encuentra la incidencia fiscalizada por ID
                if (incidencia == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(incidencia);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Incidencia> actualizarIncidencia(@PathVariable Long id,
                        @Valid @RequestBody UpdateIncidenciaRequest request) {
                
                // IF: Validación de integridad en la actualización del Plan de Vuelo
                if (request.idPlanVuelo() != null && request.idPlanVuelo() <= 0) {
                        throw new PlanVueloInvalidoException(request.idPlanVuelo(), "El ID del Plan de Vuelo modificado no es válido.");
                }

                Incidencia incidenciaActualizada = incidenciaService.updateIncidencia(IncidenciaMapper.toModel(id, request));
                
                if (incidenciaActualizada == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(incidenciaActualizada);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarIncidencia(@PathVariable Long id) {
                // IF: Validación del ID numérico wrapper de la BD antes de proceder
                if (id == null || id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                incidenciaService.deleteIncidencia(id);
                return ResponseEntity.noContent().build(); 
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalIncidencias() {
                int total = incidenciaService.totalIncidenciasV2();
                
                // IF: Si el conteo arroja un error aritmético o de sincronización en BD
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @GetMapping("/plan-vuelo/{idPlanVuelo}")
        public ResponseEntity<List<Incidencia>> selectPorPlanVuelo(@PathVariable Long idPlanVuelo) {
                // IF: Validación del parámetro de la URL
                if (idPlanVuelo == null || idPlanVuelo <= 0) {
                        throw new PlanVueloInvalidoException(idPlanVuelo, "El ID de plan de vuelo consultado en la URL es inválido.");
                }

                List<Incidencia> incidencias = incidenciaService.obtenerPorPlanVuelo(idPlanVuelo);
                
                // IF: Si no se encontraron reportes ciudadanos o de mandantes asociados a ese plan
                if (incidencias.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incidencias);
                }
                
                return ResponseEntity.ok(incidencias);
        }
}