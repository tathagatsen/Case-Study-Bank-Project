package com.project.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse {
	private boolean success;
	private String message;
	private Object data;
	public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}	
