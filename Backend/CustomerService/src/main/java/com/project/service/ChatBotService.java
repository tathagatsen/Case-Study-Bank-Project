package com.project.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChatBotService {
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	private final WebClient webClient;
	
	public ChatBotService(WebClient.Builder webClient) {
		super();
		this.webClient = webClient.build();
	}

	public String getResponse(String question) {
		// TODO Auto-generated method stub
		Map<String,Object> requestBody=Map.of(
					"contents",new Object[] {
							Map.of("parts",new Object[] {
									Map.of("text",question)
							})
					}
				);
		String response= webClient.post()
			.uri(geminiApiUrl+geminiApiKey)
			.header("Content-Type", "application/json")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		return response;
	}

}
