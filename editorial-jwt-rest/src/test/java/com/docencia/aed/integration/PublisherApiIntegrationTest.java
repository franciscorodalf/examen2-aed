package com.docencia.aed.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PublisherApiIntegrationTest extends IntegrationTestBase {

    // ===== GET /publishers =====

    @Test
    void getPublishersUnauthorizedTest() {
        getServiceUnauthorizedTest(get("/api/v2/publishers", null));
    }

    @Test
    void getPublishersOkTest() {
        String token = loginAndGetToken();
        var res = get("/api/v2/publishers", token);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().startsWith("["));
    }

    @Test
    void getPublishersInvalidTokenTest() {
        getServiceUnauthorizedTest(get("/api/v2/publishers", "bad"));
    }

    // ===== POST /publishers =====

    @Test
    void createPublisherUnauthorizedTest() {
        getServiceUnauthorizedTest(postJson("/api/v2/publishers", null, Map.of("name", "Nueva", "city", "Madrid")));
    }

    @Test
    void createPublisherOkTest() {
        String token = loginAndGetToken();
        var res = postJson("/api/v2/publishers", token, Map.of("name", "Nueva", "city", "Madrid"));
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertNotNull(res.getBody());
        assertTrue(res.getBody().contains("\"name\"") || res.getBody().contains("Nueva"));
    }

    @Test
    void createPublisherInvalidTokenTest() {
        var res = postJson("/api/v2/publishers", "bad", Map.of("name", "X", "city", "Y"));
        assertTrue(res.getStatusCode() == HttpStatus.UNAUTHORIZED || res.getStatusCode() == HttpStatus.FORBIDDEN);
    };

}
