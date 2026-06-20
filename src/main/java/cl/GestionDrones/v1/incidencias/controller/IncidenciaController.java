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
import cl.GestionDrones.v1.incidencias.model.Incidencia;
import cl.GestionDrones.v1.incidencias.service.IncidenciaService;
import cl.GestionDrones.v1.incidencias.exception.PlanVueloInvalidoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Incidencias", description = "Operaciones relacionadas con las incidencias reportadas en vuelo")
@RestController
@RequestMapping("/api/v1/incidencias")
public class IncidenciaController {

        private final IncidenciaService incidenciaService;

        public IncidenciaController(IncidenciaService incidenciaService) {
                this.incidenciaService = incidenciaService;
        }

        @Operation(summary = "Obtener todas las incidencias", description = "Retorna una lista completa de todas las incidencias registradas en el sistema")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de incidencias obtenida con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "204", description = "No hay contenido disponible (Lista vacía)", content = @Content)
        })
        @GetMapping
        public ResponseEntity<List<Incidencia>> listarIncidencias() {
                List<Incidencia> incidencias = incidenciaService.getIncidencias();
                
                if (incidencias.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(incidencias);
        }

        @Operation(summary = "Agregar una nueva incidencia", description = "Registra una nueva incidencia validando el ID de plan de vuelo")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Incidencia registrada de manera exitosa", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "400", description = "ID de plan de vuelo inválido o datos de entrada incorrectos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
        @PostMapping
        public ResponseEntity<Incidencia> agregarIncidencia(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON de la nueva incidencia a reportar", required = true)
            @Valid @RequestBody CreateIncidenciaRequest request
        ) {
                
                if (request.idPlanVuelo() != null && request.idPlanVuelo() <= 0) {
                        throw new PlanVueloInvalidoException(request.idPlanVuelo(), "El ID del Plan de Vuelo asociado debe ser un identificador numérico válido.");
                }

                Incidencia nuevaIncidencia = incidenciaService.saveIncidencia(request);
                
                if (nuevaIncidencia == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaIncidencia);
        }

        @Operation(summary = "Buscar incidencia por ID", description = "Obtiene los detalles individuales de una incidencia usando su identificador numérico")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencia encontrada", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<Incidencia> buscarIncidencia(
            @Parameter(description = "ID único de la incidencia a buscar", required = true, example = "1")
            @PathVariable Long id
        ) {
                Incidencia incidencia = incidenciaService.getIncidenciaId(id);
                
                if (incidencia == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(incidencia);
        }

        @Operation(summary = "Actualizar incidencia", description = "Modifica los datos de una incidencia existente de acuerdo con su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencia actualizada correctamente", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "400", description = "Plan de vuelo inválido o payload de entrada incorrecto", content = @Content),
            @ApiResponse(responseCode = "404", description = "La incidencia especificada no existe", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<Incidencia> actualizarIncidencia(
            @Parameter(description = "ID de la incidencia que se desea actualizar", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON con los nuevos datos de la incidencia", required = true)
            @Valid @RequestBody UpdateIncidenciaRequest request
        ) {
                
                if (request.idPlanVuelo() != null && request.idPlanVuelo() <= 0) {
                        throw new PlanVueloInvalidoException(request.idPlanVuelo(), "El ID del Plan de Vuelo modificado no es válido.");
                }
                Incidencia incidenciaActualizada = incidenciaService.updateIncidencia(id, request);
                if (incidenciaActualizada == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(incidenciaActualizada);
        }

        @Operation(summary = "Eliminar incidencia", description = "Elimina físicamente una incidencia del sistema a partir de su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Incidencia eliminada con éxito"),
            @ApiResponse(responseCode = "400", description = "ID proporcionado es nulo o inválido", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarIncidencia(
            @Parameter(description = "ID de la incidencia que se desea eliminar", required = true, example = "1")
            @PathVariable Long id
        ) {
                
                if (id == null || id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                incidenciaService.deleteIncidencia(id);
                return ResponseEntity.noContent().build(); 
        }

        @Operation(summary = "Obtener total de incidencias", description = "Devuelve un conteo numérico absoluto de las incidencias registradas")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo realizado con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
        @GetMapping("/total")
        public ResponseEntity<Integer> totalIncidencias() {
                int total = incidenciaService.totalIncidencias();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @Operation(summary = "Obtener incidencias por Plan de Vuelo", description = "Lista todas las incidencias asociadas a un Plan de Vuelo")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencias devueltas correctamente", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "204", description = "El Plan de Vuelo no tiene incidencias registradas", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID del Plan de Vuelo proporcionado es inválido", content = @Content)
        })
        @GetMapping("/plan-vuelo/{idPlanVuelo}")
        public ResponseEntity<List<Incidencia>> selectPorIdPlanVuelo(
            @Parameter(description = "ID del plan de vuelo de referencia", required = true, example = "101")
            @PathVariable Long idPlanVuelo
        ) {
                if (idPlanVuelo == null || idPlanVuelo <= 0) {
                        throw new PlanVueloInvalidoException(idPlanVuelo, "El ID de plan de vuelo consultado en la URL es inválido.");
                }

                List<Incidencia> incidencias = incidenciaService.obtenerPorPlanVuelo(idPlanVuelo);
                
                if (incidencias == null || incidencias.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(incidencias);
        }

        @Operation(summary = "Buscar incidencias por origen", description = "Retorna una lista de incidencias filtradas por el origen del reporte")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencias encontradas de manera correcta", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Incidencia.class))),
            @ApiResponse(responseCode = "204", description = "No existen incidencias reportadas con el origen consultado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Origen del reporte vacío o inválido", content = @Content)
        })
        @GetMapping("/origen/{origenReporte}")
        public ResponseEntity<List<Incidencia>> buscarPorOrigen(
            @Parameter(description = "Nombre o identificador del origen de reporte", required = true, example = "SISTEMA_PILOTO")
            @PathVariable String origenReporte
        ) {
                if (origenReporte == null || origenReporte.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Incidencia> incidencias = incidenciaService.obtenerPorOrigen(origenReporte);

                if (incidencias.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }

                return ResponseEntity.ok(incidencias);
        }
}