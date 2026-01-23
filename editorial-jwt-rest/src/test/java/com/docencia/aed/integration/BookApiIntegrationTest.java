package com.docencia.aed.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookApiIntegrationTest extends IntegrationTestBase {

    // ===== GET /books =====

    @Test
    void getBooksUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/books", null));
    }

    @Test
    void getBooksOkTest() {
        String token = loginAndGetToken();
        var res = get("/books", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().startsWith("["));
    }

    @Test
    void getBooksInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/books", "bad"));
    }

    // ===== GET /books/{id} =====

    @Test
    void getBookByIdUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/books/1", null));
    }

    @Test
    void getBookByIdOkTest() {
        String token = loginAndGetToken();
        var res = get("/books/1", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("\"id\":1") || res.getBody().contains("\"id\": 1"));
    }

    @Test
    void getBookByIdInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/books/1", "invalid"));
    }

    // ===== GET /books?authorId=1 =====

    @Test
    void searchBooksByAuthorUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/books?authorId=1", null));
    }

    @Test
    void searchBooksByAuthorOkTest() {
        String token = loginAndGetToken();
        var res = get("/books?authorId=1", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().startsWith("["));
    }

    @Test
    void searchBooksByAuthorInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/books?authorId=1", "bad"));
    }

    // ===== POST /books?authorId=1 =====

    @Test
    void createBookUnauthorizedTest() {
        getServiceUnauthorizedTest(postJson("/books?authorId=1", null, Map.of("title", "Libro Nuevo", "publicationYear", 2025)));
    }

    @Test
    void createBookOkTest() {
        String token = loginAndGetToken();
        var res = postJson("/books?authorId=1", token, Map.of("title", "Libro Nuevo", "publicationYear", 2025));
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("\"title\"") || res.getBody().contains("Libro Nuevo"));
    }

    @Test
    void createBookInvalidTokenTest() {
       getServiceUnauthorizedTest(postJson("/books?authorId=1", "bad", Map.of("title", "X", "publicationYear", 2020)));
    }
}
