package vn.atdigital.ftpservice.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * Handling Protected Content.
 */
@Configuration
public class ResourceServerConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   JwtAuthenticationConverter jwtAuthConverter) throws Exception {

		http
				.csrf(AbstractHttpConfigurer::disable)
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/swagger-ui.html",
								"/swagger-ui/**",
								"/v3/api-docs/**",
								"/webjars/**"
						).permitAll()
//						.requestMatchers("/ftp/**").hasAuthority("SCOPE_read")
//						.requestMatchers("/ftp/**").hasAuthority("SCOPE_write")
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
				);

		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter(CustomAccessTokenConverter customAccessTokenConverter) {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(customAccessTokenConverter);
		return converter;
	}

	@Bean
	public JwtDecoder jwtDecoder() throws Exception {
		String publicKeyPEM = new String(
				Objects.requireNonNull(ResourceServerConfig.class.getResourceAsStream("/certificate/pubkey.txt"))
						.readAllBytes(), StandardCharsets.UTF_8);

		// Clean BEGIN/END lines if necessary
		publicKeyPEM = publicKeyPEM
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "");

		byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
		java.security.spec.X509EncodedKeySpec keySpec = new java.security.spec.X509EncodedKeySpec(decoded);
		java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");

		java.security.PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return NimbusJwtDecoder.withPublicKey((java.security.interfaces.RSAPublicKey) publicKey).build();
	}
}
