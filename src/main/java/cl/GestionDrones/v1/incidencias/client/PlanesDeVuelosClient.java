package cl.GestionDrones.v1.incidencias.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.GestionDrones.v1.incidencias.exception.PlanVueloInvalidoException;

@Component
public class PlanesDeVuelosClient {

private final WebClient webClient;

    public PlanesDeVuelosClient(@Qualifier("planesDeVuelosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void verificarPlanExiste(Long idPlanVuelo) {
        try {
            webClient.get()
                    .uri("/{id}", idPlanVuelo)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            
            throw new PlanVueloInvalidoException(idPlanVuelo,
                "El plan de vuelo con ID " + idPlanVuelo + " no existe en el sistema.");

        } catch (WebClientResponseException e) {
            
            throw new PlanVueloInvalidoException(idPlanVuelo,
                "Error al consultar el plan de vuelo con ID " + idPlanVuelo 
                + ". Código: " + e.getStatusCode());

        } catch (Exception e) {
            throw new RuntimeException(
                "No se puede conectar con el servicio de Planes de Vuelo. Verifique que esté activo.");
        }
    }
}
