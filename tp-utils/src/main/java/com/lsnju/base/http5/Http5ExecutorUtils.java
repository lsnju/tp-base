package com.lsnju.base.http5;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/9/8 15:40
 * @version V1.0
 */
@Slf4j
public class Http5ExecutorUtils {

    public static final Executor TRUST_ALL_EXECUTOR = newTrustAllInstance2();

    public static Executor newTrustAllInstance() {
        try {
            final SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .build();
            final SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslcontext)
                .build();
            final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
            final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
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
            final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build();

            final LayeredConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            final Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", ssl)
                .build();

            final PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(sfr);
            connMgr.setDefaultMaxPerRoute(100);
            connMgr.setMaxTotal(200);
            connMgr.setValidateAfterInactivity(TimeValue.ofMilliseconds(2000));

            final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connMgr)
                .build();
            return Executor.newInstance(httpClient);
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return Executor.newInstance();
        }
    }

}
