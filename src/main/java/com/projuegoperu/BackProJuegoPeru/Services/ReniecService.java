package com.projuegoperu.BackProJuegoPeru.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecService {
    
    @Value("${reniec.api.url}")
    private String apiUrl;

    @Value("${reniec.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ReniecService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String consultarDNI(String dni) {
        // Construir la URL sin '/consulta-dni/'
        String url = String.format("%s?numero=%s", apiUrl, dni);
        
        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey); // O utiliza el encabezado correcto según la documentación de la API
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // Realizar la solicitud GET con los encabezados
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        
        return response.getBody();
    }
}