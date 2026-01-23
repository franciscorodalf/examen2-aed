package com.docencia.aed.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthorApiIntegrationTest extends IntegrationTestBase {

    // ===== GET /authors =====

    @Test
    void getAuthorsUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/authors", null));
    }

    @Test
    void getAuthorsOkTest() {
        String token = loginAndGetToken();
        var res = get("/authors", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().startsWith("[")); // lista JSON
    }

    @Test
    void getAuthorsInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/authors", "invalid.token.here"));
    }

    // ===== GET /authors/{id} =====

    @Test
    void getAuthorByIdUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/authors/1", null));
    }

    @Test
    void getAuthorByIdOkTest() {
        String token = loginAndGetToken();
        var res = get("/authors/1", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("\"id\":1") || res.getBody().contains("\"id\": 1"));
    }

    @Test
    void getAuthorByIdInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/authors/1", "bad"));
    }

    // ===== GET /authors/{id}/books =====

    @Test
    void getAuthorBooksUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/authors/1/books", null));
    }

    @Test
    void getAuthorBooksOkTest() {
        String token = loginAndGetToken();
        var res = get("/authors/1/books", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().startsWith("["));
    }

    @Test
    void getAuthorBooksInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/authors/1/books", "invalid"));
    }

    // ===== POST /authors =====

    @Test
    void createAuthorUnauthorizedTest() {
        getServiceUnauthorizedTest(postJson("/authors", null, Map.of("name", "Test", "country", "ES")));
    }
    @Test
    void createAuthorOkTest() {
        String token = loginAndGetToken();
        var res = postJson("/authors", token, Map.of("name", "Autor Nuevo", "country", "ES"));
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("\"name\"") || res.getBody().contains("Autor Nuevo"));
    }

    @Test
    void createAuthorInvalidTokenTest() {
        getServiceUnauthorizedTest(postJson("/authors", "bad", Map.of("name", "X", "country", "Y")));
    }

}
