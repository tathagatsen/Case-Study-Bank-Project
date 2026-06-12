package com.nexbank.authservice.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

@Configuration
public class ClientConfig {
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient bankingClient= RegisteredClient.withId(UUID.randomUUID().toString()).clientId("api-gateway").clientSecret("{noop}secret123")
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.scope("accounts.read")
			.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(java.time.Duration.ofMinutes(30)).build())
			.build();
			return new InMemoryRegisteredClientRepository(bankingClient);
	}
}
