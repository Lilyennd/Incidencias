package cl.GestionDrones.v1.incidencias.exception;

/**
 * Excepción de negocio lanzada cuando el ID de un Plan de Vuelo 
 * no es válido o no arrojó resultados de incidencias en el sistema de la DGAC.
 */
public class PlanVueloInvalidoException extends RuntimeException {
    
    private final Long idPlanVuelo;

    /**
     * Constructor con mensaje predefinido estándar de la DGAC.
     */
    public PlanVueloInvalidoException(Long idPlanVuelo) {
        super(String.format("El ID de Plan de Vuelo '%d' no es válido o no arrojó resultados de incidencias en el sistema.", idPlanVuelo));
        this.idPlanVuelo = idPlanVuelo;
    }

    /**
     * Constructor sobrecargado para pasar un mensaje personalizado desde el Controller o Service.
     */
    public PlanVueloInvalidoException(Long idPlanVuelo, String mensajePersonalizado) {
        super(mensajePersonalizado);
        this.idPlanVuelo = idPlanVuelo;
    }

    /**
     * Getter para que el GlobalExceptionHandler pueda capturar el ID 
     * y mostrarlo en el JSON de respuesta de Problem Details.
     */
    public Long getIdPlanVuelo() {
        return idPlanVuelo;
    }
}
