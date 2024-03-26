package com.lsnju.base.http;

import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.junit.jupiter.api.Test;

import com.lsnju.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023/8/25 12:35
 * @version V1.0
 */
@Slf4j
public class TpHttpClientUtilsFunTest {

    @Test
    void test_001() {
        final String targetUrl = "http://localhost:8080/tp/mo/sysinfo.json";
        try {
            final ClassicHttpResponse response = TpHttpClientUtils.HTTP_CLIENT.get(targetUrl);
            final int statusCode = response.getCode();
            final String rawResp = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
                if (JsonUtils.isValidJson(rawResp)) {
                    log.info("{}", JsonUtils.toPrettyFormat(rawResp));
                }
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_002() {
        try {
            TpHttpClient CLIENT = TpHttpClientUtils.HTTP_CLIENT;
            final String targetUrl = "http://localhost:8080/tp/mo/sysinfo.json";
            final ClassicHttpResponse response = CLIENT.get(targetUrl);
            final int statusCode = response.getCode();
            final String rawResp = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
                if (JsonUtils.isValidJson(rawResp)) {
                    log.info("{}", JsonUtils.toPrettyFormat(rawResp));
                }
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_httpclient5_direct() {

        try {
            ConnectionConfig connConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(7_000L))
                .setSocketTimeout(Timeout.ofMilliseconds(20_000L))
                .build();

            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(2000L))
                .build();

            BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
            cm.setConnectionConfig(connConfig);

            CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm)
                .build();

            final String rawRespStr = httpClient.execute(new HttpGet("http://localhost:8080/tp/mo/sysinfo.json"), response -> {
                final int statusCode = response.getCode();
                final String rawResp = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                if (log.isInfoEnabled()) {
                    log.info("code = {}, rawResp = {}", statusCode, rawResp);
                }
                return rawResp;
            });
            log.info("{}", rawRespStr);

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }
}
