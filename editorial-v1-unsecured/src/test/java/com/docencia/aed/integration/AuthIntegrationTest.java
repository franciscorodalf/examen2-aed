package com.docencia.aed.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthIntegrationTest extends IntegrationTestBase {

    @Test
    void loginOkTest() {
        String token = loginAndGetToken();
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void loginBadCredentialsTest() {
        ResponseEntity<String> res = rest.postForEntity(
                url("/api/auth/login"),
                Map.of("username", "admin", "password", "wrong"),
                String.class
        );
        // Spring Security suele devolver 401 si falla auth
        getServiceUnauthorizedTest(res);
    }

    @Test
    void loginValidationTest() {
        ResponseEntity<String> res = rest.postForEntity(
                url("/api/auth/login"),
                Map.of("username", "", "password", ""),
                String.class
        );
        // @Valid + @NotBlank => 400
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }
}
