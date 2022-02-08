package com.example.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ConsultationRestClient {
    private RestTemplate restTemplate;
    private ConsultationRestClientProperties properties;

    public ConsultationRestClient(ConsultationRestClientProperties properties) {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new ConsultationErrorHandler());
        this.properties = properties;
    }

    public Iterable<Consultation> findAll() throws URISyntaxException {
        RequestEntity<Iterable<Consultation>> requestEntity = new RequestEntity
                <Iterable<Consultation>>(HttpMethod.GET,new URI(properties.getUrl() +
                properties.getBasePath()));
        ResponseEntity<Iterable<Consultation>> response =
                restTemplate.exchange(requestEntity,new
                        ParameterizedTypeReference<Iterable<Consultation>>(){});
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        return null;
    }

    public Consultation findById(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return restTemplate.getForObject(properties.getUrl() +
                properties.getBasePath() + "/{id}",Consultation.class,params);
    }

    public Consultation upsert(Consultation toDo) throws URISyntaxException {
        RequestEntity<?> requestEntity = new RequestEntity<>(toDo, HttpMethod.
                POST, new URI(properties.getUrl() + properties.getBasePath()));
        ResponseEntity<?> response = restTemplate.exchange(requestEntity, new
                ParameterizedTypeReference<Consultation>() {});
        if(response.getStatusCode() == HttpStatus.CREATED){
            return restTemplate.getForObject(Objects.requireNonNull(response.getHeaders().
                    getLocation()),Consultation.class);
        }
        return null;
    }

    public Consultation setCompleted(String id) throws URISyntaxException{
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        restTemplate.postForObject(properties.getUrl() + properties.getBasePath() +
                "/{id}?_method=patch",null,ResponseEntity.class, params);
        return findById(id);
    }

    public void delete(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        restTemplate.delete(properties.getUrl() + properties.getBasePath() +
                "/{id}",params);
    }
}
