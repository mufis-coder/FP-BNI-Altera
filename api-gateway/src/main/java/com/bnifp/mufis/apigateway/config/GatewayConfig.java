package com.bnifp.mufis.apigateway.config;
import com.bnifp.mufis.apigateway.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("post", r -> r.path("/posts/**").filters(f -> f.filter(filter)).uri("lb://post" +
                        "-service"))
                .route("user", r->r.path("/users/**").filters(f -> f.filter(filter)).uri("lb://auth-service"))
                .route("auth", r->r.path("/auth/**").uri("lb://auth-service"))
                .route("post-swagger", r->r.path("/post-swagger/**").uri("lb://post-service"))
                .route("auth-swagger", r->r.path("/auth-swagger/**").uri("lb://auth-service"))
                .route("log-swagger", r->r.path("/log-swagger/**").uri("lb://log-service"))
                .build();
//                .route("auth", r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("lb://auth"))
//                .route("order-service", r -> r.path("/order/**").filters(f -> f.filter(filter)).uri("lb://order" +
//                        "-service")).build();
    }

}

