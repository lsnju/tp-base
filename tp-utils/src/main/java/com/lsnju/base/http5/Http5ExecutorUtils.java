package com.lsnju.base.http5;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;

import com.lsnju.base.http5.log.TpHttp5RequestInterceptor;
import com.lsnju.base.http5.log.TpHttp5ResponseInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/9/8 15:40
 * @version V1.0
 */
@Slf4j
public class Http5ExecutorUtils {

    public static final Executor TRUST_ALL_EXECUTOR = newTrustAllInstance();

    public static Executor newTrustAllInstance() {
        try {
            var sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                .build();

            var tlsStrategy = new DefaultClientTlsStrategy(
                sslContext,
                HostnameVerificationPolicy.CLIENT,
                NoopHostnameVerifier.INSTANCE);

            var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsStrategy)
                .build();

            var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                .build();
            return Executor.newInstance(httpClient);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error(String.format("%s", e.getMessage()), e);
            return Executor.newInstance();
        }
    }

    public static Executor newTrustAllInstance2() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build();

            TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(
                sslContext,
                HostnameVerificationPolicy.CLIENT,
                NoopHostnameVerifier.INSTANCE);

            PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsStrategy)
                .setMaxConnPerRoute(100)
                .setMaxConnTotal(200)
                .setConnectionConfigResolver(x -> ConnectionConfig.custom()
                    .setValidateAfterInactivity(TimeValue.ofMilliseconds(2000)).build())
                .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                .build();
            return Executor.newInstance(httpClient);
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return Executor.newInstance();
        }
    }

    public static Executor defaultExecutor() {
        try {
            final CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                    .useSystemProperties()
                    .setMaxConnPerRoute(100)
                    .setMaxConnTotal(200)
                    .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                        .build())
                    .build())
                .useSystemProperties()
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofMinutes(1))
                .addRequestInterceptorLast(new TpHttp5RequestInterceptor())
                .addResponseInterceptorFirst(new TpHttp5ResponseInterceptor())
                .build();
            return Executor.newInstance(httpClient);
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return Executor.newInstance();
        }
    }

}
