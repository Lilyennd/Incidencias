package cl.GestionDrones.v1.incidencias.webClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Value("${api.url.planesdevuelos}")
    private String planesDeVuelosUrl;

    @Bean
    public WebClient planesDeVuelosWebClient() {
        return WebClient.builder()
                .baseUrl(planesDeVuelosUrl)
                .build();
    }
}