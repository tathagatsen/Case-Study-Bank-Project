package com.nexbank.authservice.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginSuccessEventListener {
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		log.info("Login Success user{} ",event.getAuthentication().getName());
		
	}
}
