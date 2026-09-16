package duoc.rutaexpress.shipments.client;

import duoc.rutaexpress.shipments.exception.BusinessRuleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CatalogClient {

    private final RestClient restClient;

    public CatalogClient(@Value("${catalog.base-url:http://localhost:8081}") String catalogBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(catalogBaseUrl).build();
    }

    public CatalogServiceSnapshot reserveCapacity(Long serviceId) {
        try {
            CatalogServiceSnapshot service = restClient.put()
                    .uri("/api/catalog/services/{id}/capacity/decrease", serviceId)
                    .retrieve()
                    .body(CatalogServiceSnapshot.class);
            if (service == null) {
                throw new BusinessRuleException("Catálogo no devolvió el servicio reservado");
            }
            return service;
        } catch (BusinessRuleException exception) {
            throw exception;
        } catch (RestClientException exception) {
            throw new BusinessRuleException("El servicio seleccionado no existe o no tiene capacidad disponible");
        }
    }
}
