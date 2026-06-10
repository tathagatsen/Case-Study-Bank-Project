package com.nexbank.gateway.filter;
import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BankingRequestLogginFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        exchange.getResponse().getHeaders().add("X-Request-Id", requestId);

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String user = exchange.getRequest()
                              .getHeaders()
                              .getFirst("X-User-Id");

        log.info("[REQ] id={} user={} {} {}",
                requestId,
                user,
                method,
                path);

        return chain.filter(exchange).doOnSuccess(v->{
        		long timeTaken=System.currentTimeMillis()-startTime;
        		log.info("[Res] id={} status={} time={}ms",requestId,exchange.getResponse().getStatusCode(),timeTaken);
        }).doOnError(ex->{
        		long timeTaken=System.currentTimeMillis()-startTime;
        		log.error("[err] id={} {} {} time={}ms eror={}",requestId,method,path,timeTaken,ex);
        })        ;
    }
}