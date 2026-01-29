package com.docencia.aed.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base para tests de integración con JWT.
 *
 * - Si existe la propiedad test.base-url, los tests apuntan a esa URL.
 * - Si no, apuntan al servidor levantado por Spring Boot en puerto aleatorio.
 */
public abstract class IntegrationTestBase {

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate rest;

    @Value("${test.base-url:}")
    private String configuredBaseUrl;

    protected String baseUrl;

    protected final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void initBaseUrl() {
        String fromSysProp = System.getProperty("test.base-url"); // permite -Dtest.base-url=...
        String chosen = (fromSysProp != null && !fromSysProp.isBlank())
                ? fromSysProp
                : configuredBaseUrl;

        baseUrl = (chosen != null && !chosen.isBlank())
                ? chosen.replaceAll("/$", "")
                : "http://localhost:" + port;
    }

    protected String url(String path) {
        assertNotNull(path);
        return baseUrl + (path.startsWith("/") ? path : ("/" + path));
    }

    protected String loginAndGetToken() {
        String loginUrl = url("/api/auth/login");
        Map<String, String> body = Map.of("username", "admin", "password", "admin123");

        ResponseEntity<String> res = rest.postForEntity(loginUrl, body, String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode(), "Login debe devolver 200");
        assertNotNull(res.getBody());

        try {
            JsonNode node = om.readTree(res.getBody());
            assertTrue(node.hasNonNull("token"), "La respuesta debe contener token");
            return node.get("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo parsear JSON del login: " + res.getBody(), e);
        }
    }

    protected HttpHeaders authHeaders(String token) {
        HttpHeaders h = new HttpHeaders();
        h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        if (token != null) {
            h.setBearerAuth(token);
        }
        return h;
    }

    protected ResponseEntity<String> get(String path, String token) {
        HttpEntity<Void> req = new HttpEntity<>(authHeaders(token));
        return rest.exchange(url(path), HttpMethod.GET, req, String.class);
    }

    protected ResponseEntity<String> postJson(String path, String token, Object body) {
        HttpHeaders h = authHeaders(token);
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> req = new HttpEntity<>(body, h);
        return rest.exchange(url(path), HttpMethod.POST, req, String.class);
    }

    void getServiceUnauthorizedTest(ResponseEntity<String> res) {
        invalidTocken(res.getStatusCode());
    }

    void invalidTocken(HttpStatusCode status) {
        assertTrue(status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN);
    }
}
