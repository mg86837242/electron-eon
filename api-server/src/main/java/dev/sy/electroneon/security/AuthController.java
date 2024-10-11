package dev.sy.electroneon.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public String token(Authentication authentication) {
        LOG.debug("Token requested for user: '{}'", authentication.getName());

        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted: {}", token);

        return token;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<Jwt> getAuthenticatedUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/identity")
    public String example(Authentication authentication) {
        System.out.println("name:  " + authentication.getName());
        System.out.println("creds: " + authentication.getCredentials());
        System.out.println("detls: " + authentication.getDetails());
        System.out.println("princ: " + authentication.getPrincipal());
        System.out.println("roles: " + authentication.getAuthorities());

        return "Hello, " + authentication.getName();
    }

    // NB For `@PreAuthorize` annot. to work, `@EnableMethodSecurity` needs
    // to be added to the `SecurityConfig` class
    // "SCOPE_" prefix comes from OAuth 2.0 spec and is used to represent the
    // scopes associated with access tokens
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/admin/admin-only")
    public String example() {
        return "Hello, admin!";
    }
}

// References:
// -- `@PreAuthorize` annot. example && main guide for JWT in Spring: https://github.com/danvega/jwt/blob/master/src/main/java/dev/danvega/jwt/controller/HomeController.java
// -- Spring Security Expressions: https://www.baeldung.com/spring-security-expressions
