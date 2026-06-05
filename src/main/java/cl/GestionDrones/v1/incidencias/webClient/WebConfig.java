package cl.GestionDrones.v1.incidencias.webClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public WebClient planesDeVuelosWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083/api/v1/planesDeVuelos")
                .build();
    }
}