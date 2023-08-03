package com.lsnju.base.http;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.fluent.Executor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2021/9/8 15:40
 * @version V1.0
 */
@Slf4j
public class HttpExecutorUtils {

    public static final Executor TRUST_ALL_EXECUTOR = newTrustAllInstance2();

    public static Executor newTrustAllInstance() {
        try {
            final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build();
            final CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext)
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
            connMgr.setValidateAfterInactivity(1000);

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
