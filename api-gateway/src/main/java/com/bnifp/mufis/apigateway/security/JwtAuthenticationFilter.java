package com.bnifp.mufis.apigateway.security;

import com.bnifp.mufis.apigateway.dto.response.BaseResponse;
import com.bnifp.mufis.apigateway.exception.JwtTokenMalformedException;
import com.bnifp.mufis.apigateway.exception.JwtTokenMissingException;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final String JWT_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //response for jwt that doesn't pass authentication and authorization filter
    private Mono<Void> notAuthenticatedAuthorized(ServerWebExchange exchange,
                                                  String msg, HttpStatus statusCode){
        ServerHttpResponse response = exchange.getResponse();

        String message = msg;
        BaseResponse responseMsg = new BaseResponse<>(Boolean.FALSE, message);

        response.setStatusCode(statusCode);
        //change object to json -> send it through response
        byte[] bytes = new Gson().toJson(responseMsg).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/register", "/login");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            //get token from Header
            final String token = getJWTFromRequest(request);

            if(!jwtTokenProvider.validateToken(token)){
                String msg = "JWT Invalid!";
                return notAuthenticatedAuthorized(exchange, msg, HttpStatus.UNAUTHORIZED);
            }

            //Forbidden check for authorization
            Claims claims = jwtTokenProvider.getClaims(token);
            String role = claims.get("role").toString();
            String route = request.getPath().toString();
            String method = request.getMethod().toString();

            if(route.equals("/users") && method.equals("GET")) {
                if(!role.equals("ADMIN")){
                    String msg = role + " is not authorized to access this resource!";
                    return notAuthenticatedAuthorized(exchange, msg, HttpStatus.FORBIDDEN);
                }
            }

            exchange.getRequest().mutate().header("username", String.valueOf(claims.get("username"))).build();
        }

        return chain.filter(exchange);
    }

    private String getJWTFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty(JWT_HEADER).get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
