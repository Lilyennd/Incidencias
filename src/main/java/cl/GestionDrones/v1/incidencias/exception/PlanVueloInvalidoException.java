package cl.GestionDrones.v1.incidencias.exception;


public class PlanVueloInvalidoException extends RuntimeException {
    
    private final Long idPlanVuelo;


    public PlanVueloInvalidoException(Long idPlanVuelo) {
        super(String.format("El ID de Plan de Vuelo '%d' no es válido o no arrojó resultados de incidencias en el sistema.", idPlanVuelo));
        this.idPlanVuelo = idPlanVuelo;
    }

    public PlanVueloInvalidoException(Long idPlanVuelo, String mensajePersonalizado) {
        super(mensajePersonalizado);
        this.idPlanVuelo = idPlanVuelo;
    }


    public Long getIdPlanVuelo() {
        return idPlanVuelo;
    }
}
