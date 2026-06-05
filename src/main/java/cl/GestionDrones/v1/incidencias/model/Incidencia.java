package cl.GestionDrones.v1.incidencias.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_plan_vuelo", nullable = true)
    private Long idPlanVuelo;

    @Column(name = "origen_reporte", nullable = false)
    private String origenReporte;

    @Column(name = "tipo_incidencia", nullable = false)
    private String tipoIncidencia; 

    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @Column(name = "fecha_hora_reporte", nullable = false)
    private LocalDateTime fechaHoraReporte;

    @Column(name = "ubicacion_referencial", nullable = false)
    private String ubicacionReferencial; 

    public Incidencia() {
    }

    public Incidencia(Long id, Long idPlanVuelo, String origenReporte, String tipoIncidencia, String descripcion, LocalDateTime fechaHoraReporte, String ubicacionReferencial) {
        this.id = id;
        this.idPlanVuelo = idPlanVuelo;
        this.origenReporte = origenReporte;
        this.tipoIncidencia = tipoIncidencia;
        this.descripcion = descripcion;
        this.fechaHoraReporte = fechaHoraReporte;
        this.ubicacionReferencial = ubicacionReferencial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPlanVuelo() {
        return idPlanVuelo;
    }

    public void setIdPlanVuelo(Long idPlanVuelo) {
        this.idPlanVuelo = idPlanVuelo;
    }

    public String getOrigenReporte() {
        return origenReporte;
    }

    public void setOrigenReporte(String origenReporte) {
        this.origenReporte = origenReporte;
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHoraReporte() {
        return fechaHoraReporte;
    }

    public void setFechaHoraReporte(LocalDateTime fechaHoraReporte) {
        this.fechaHoraReporte = fechaHoraReporte;
    }

    public String getUbicacionReferencial() {
        return ubicacionReferencial;
    }

    public void setUbicacionReferencial(String ubicacionReferencial) {
        this.ubicacionReferencial = ubicacionReferencial;
    }
}
