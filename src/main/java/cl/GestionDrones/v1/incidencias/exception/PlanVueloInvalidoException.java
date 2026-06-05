package cl.GestionDrones.v1.incidencias.exception;

public class PlanVueloInvalidoException extends RuntimeException {

    private final Long idPlanVuelo;

    public PlanVueloInvalidoException(Long idPlanVuelo, String mensaje) {
        super(mensaje);
        this.idPlanVuelo = idPlanVuelo;
    }

    public Long getIdPlanVuelo() {
        return this.idPlanVuelo;
    }
}