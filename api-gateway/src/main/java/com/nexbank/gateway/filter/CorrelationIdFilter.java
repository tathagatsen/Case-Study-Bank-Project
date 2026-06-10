package com.nexbank.gateway.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter,Ordered{
	private static final String CORRELATIONID="X-Correlation-Id";

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -2;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		String currentCorrelationId=exchange.getRequest().getHeaders().getFirst(CORRELATIONID);
		if(currentCorrelationId==null) {
			currentCorrelationId=UUID.randomUUID().toString();
			exchange.getResponse().getHeaders().add(CORRELATIONID, currentCorrelationId);
		}
		return chain.filter(exchange);
	}
}
