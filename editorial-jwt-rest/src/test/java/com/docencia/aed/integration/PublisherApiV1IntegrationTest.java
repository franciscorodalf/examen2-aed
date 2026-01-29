package com.docencia.aed.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PublisherApiV1IntegrationTest extends IntegrationTestBase {

    // ===== GET /api/v1/publishers =====

    @Test
    void getPublishersV1UnauthorizedTest() {
        // V1 should NOT require token
        var res = get("/api/v1/publishers", null);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
    }

    // ===== POST /api/v1/publishers =====

    @Test
    void createPublisherV1UnauthorizedTest() {
        // V1 should NOT require token
        var res = postJson("/api/v1/publishers", null, Map.of("name", "Editorial V1", "city", "Valencia"));
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }
}
