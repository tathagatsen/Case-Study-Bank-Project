package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
@Configuration
@EnableWebSecurity
public class UserConfig {
	 	@Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 	
	 	 @Bean
	     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	         http
	             .csrf(csrf -> csrf.disable()) // 🚨 disable CSRF for APIs
	             .authorizeHttpRequests(auth -> auth
	                 .requestMatchers("/user/register", "/user/register/verification","/user/login","/user/login/verification","/user/updPwd","/user/verifyUpd","/user/enable2fa").permitAll()
	                 .anyRequest().authenticated()
	             )
	             .formLogin(login -> login.disable())
	             .httpBasic(basic -> basic.disable());

	         return http.build();
	     }
}
