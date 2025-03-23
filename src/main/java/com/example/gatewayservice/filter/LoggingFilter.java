package com.example.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {


//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter baseMessage : {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("Global PRE filter Start : request id -> {}", request.getId());
//            }
//            // Custom Post Filter
//            return chain.filter(exchange).then(
//                    Mono.fromRunnable(
//                            () -> {
//                                if (config.isPostLogger()) {
//                                    log.info("Global POST filter : response id -> {}", response.getStatusCode());
//                                }
//                            }
//                    )
//            );
//        };

        GatewayFilter filter = new OrderedGatewayFilter(
                (exchange, chain) -> {
                    ServerHttpRequest request = exchange.getRequest();
                    ServerHttpResponse response = exchange.getResponse();

                    log.info("LoggingFilter Filter baseMessage : {}", config.getBaseMessage());

                    if (config.isPreLogger()) {
                        log.info("LoggingFilter PRE filter Start : request id -> {}", request.getURI());
                    }
                    // Custom Post Filter
                    return chain.filter(exchange).then(
                            Mono.fromRunnable(
                                    () -> {
                                        if (config.isPostLogger()) {
                                            log.info("LoggingFilter POST filter : response id -> {}", response.getStatusCode());
                                        }
                                    }
                            )
                    );
                }
                , Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }


    @Data
    public static class Config {
        // Configuration 정보 넣기
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
