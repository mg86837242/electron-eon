package dev.sy.electroneon.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final RsaKeyProperties rsaKeys;

        public SecurityConfig(RsaKeyProperties rsaKeys) {
                this.rsaKeys = rsaKeys;
        }

        @Bean
        JwtDecoder jwtDecoder() {
                return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
        }

        @Bean
        JwtEncoder jwtEncoder() {
                JWK jwk = new RSAKey.Builder(rsaKeys.publicKey())
                                .privateKey(rsaKeys.privateKey())
                                .build();
                JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
                return new NimbusJwtEncoder(jwks);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/admin/**")
                                                .hasAuthority("SCOPE_ADMIN")
                                                .requestMatchers("/customer/**")
                                                .hasAnyAuthority("SCOPE_ADMIN", "SCOPE_CUSTOMER")
                                                .anyRequest()
                                                .permitAll())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .oauth2ResourceServer(server -> server.jwt(withDefaults()))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                                .build();
        }

        /*
         * This will allow the /auth/login endpoint to use basic auth and
         * everything else uses the SFC above
         */
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @Bean
        SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(AbstractHttpConfigurer::disable)
                                .securityMatcher(new AntPathRequestMatcher("/auth/login"))
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .httpBasic(withDefaults())
                                .exceptionHandling(ex -> {
                                        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                                        ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                                })
                                .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // The following link can be found by running `copilot svc show -n client`
                configuration.setAllowedOrigins(List.of(
                                "http://electr-publi-azlhzncu2erh-1647396286.us-east-1.elb.amazonaws.com"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(List.of("*"));
                configuration.setAllowedMethods(List.of(
                                "GET",
                                "POST",
                                "PATCH",
                                "DELETE"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}

// References:
// -- bare-bones `SecurityConfig`:
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
// -- `createDelegatingPasswordEncoder()`:
// https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-dpe
// -- `requestMatchers()`:
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#match-requests