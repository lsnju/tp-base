package com.lsnju.tpbase.web.gw;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.lsnju.tpbase.web.filter.reactive.RxLogUtils;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author lisong
 * @since 2021/11/26 15:23
 * @version V1.0
 */
@Slf4j
public class TpVerifyAuthTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<TpVerifyAuthTokenGatewayFilterFactory.Config> {

    public static final String KEY = "key";

    public TpVerifyAuthTokenGatewayFilterFactory() {
        super(Config.class);
    }

    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(KEY);
    }

    @Override
    public GatewayFilter apply(final Config config) {
        final JWSVerifier verifier = Objects.requireNonNull(getVerify(config));
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                final String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                RxLogUtils.logWithContext(exchange, () -> log.info("token = `{}`, token is null = {}", token, token == null));
                if (StringUtils.isNotBlank(token)) {
                    try {
                        final SignedJWT jwt = SignedJWT.parse(token);
                        if (jwt.verify(verifier)) {
                            log.info("{}", jwt.getJWTClaimsSet());
                            ServerHttpRequest request = exchange.getRequest().mutate()
                                .header("pid", jwt.getJWTClaimsSet().getStringClaim("pid"))
                                .header("uid", jwt.getJWTClaimsSet().getStringClaim("uid"))
                                .header("token_id", jwt.getJWTClaimsSet().getStringClaim("id"))
                                .header("tp_pid", jwt.getJWTClaimsSet().getStringClaim("pid"))
                                .header("tp_uid", jwt.getJWTClaimsSet().getStringClaim("uid"))
                                .header("tp_token_id", jwt.getJWTClaimsSet().getStringClaim("id"))
                                .build();
                            return chain.filter(exchange.mutate().request(request).build());
                        }
                    } catch (Exception e) {
                        log.error(String.format("%s", e.getMessage()), e);
                    }
                }
                return chain.filter(exchange);
            }

            @Override
            public String toString() {
                return filterToStringCreator(TpVerifyAuthTokenGatewayFilterFactory.this)
                    .append(KEY, config.getKey())
                    .toString();
            }

        };


    }

    @SneakyThrows
    private JWSVerifier getVerify(Config config) {
        log.info("{}", config);
        return new MACVerifier(Base64.getDecoder().decode(config.getKey()));
    }


    @Data
    public static class Config {
        private String key;
    }
}
