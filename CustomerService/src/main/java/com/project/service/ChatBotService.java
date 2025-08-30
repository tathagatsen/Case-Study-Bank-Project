package com.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	public String getResponse(String question) {
		// TODO Auto-generated method stub
		return null;
	}

}
