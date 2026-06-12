package com.nexbank.authservice.config;


import java.security.KeyPair;
import java.security.KeyPairGenerator;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwkConfig {
	@Bean
	JWKSource<SecurityContext> jwkSource() throws Exception{
		
		KeyPairGenerator generator= KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair= generator.generateKeyPair();	
		RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey=(RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey=new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
		JWKSet jwkSet=new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}
	
}
