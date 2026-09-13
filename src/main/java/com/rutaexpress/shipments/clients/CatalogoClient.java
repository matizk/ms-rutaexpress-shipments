package com.rutaexpress.shipments.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogoClient {

    private final WebClient webClient;

    @Value("${catalogo.url}")
    private String catalogoUrl;

    public CatalogoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void disminuirCapacidad(Long servicioId) {

        webClient.put()
                .uri(catalogoUrl
                        + "/api/catalog/services/"
                        + servicioId
                        + "/capacity/decrease")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}