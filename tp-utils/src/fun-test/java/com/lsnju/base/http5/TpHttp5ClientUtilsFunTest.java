package com.lsnju.base.http5;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.TimeValue;
import org.junit.jupiter.api.Test;

import com.lsnju.base.http5.log.TpHttp5RequestInterceptor;
import com.lsnju.base.http5.log.TpHttp5ResponseInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/1/10 10:13
 * @version V1.0
 */
@Slf4j
public class TpHttp5ClientUtilsFunTest {

    public static final String URL = "http://localhost:8080/tp/mo/sysinfo.json";

    @Test
    void test_001() {
        try {
            try (ClassicHttpResponse response = TpHttp5ClientUtils.get(URL)) {
                final int statusCode = response.getCode();
                final String rawRespStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                if (log.isInfoEnabled()) {
                    log.info("code = {}", statusCode);
                    log.info("resp.length = {}", StringUtils.length(rawRespStr));
                }
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }


    @Test
    void test_executor() {
        try {
            final CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                    .useSystemProperties()
                    .setMaxConnPerRoute(100)
                    .setMaxConnTotal(200)
                    .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                    .build())
                .useSystemProperties()
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofMinutes(1))
                .addRequestInterceptorLast(new TpHttp5RequestInterceptor())
                .addResponseInterceptorFirst(new TpHttp5ResponseInterceptor())
                .build();

            Executor executor = Executor.newInstance(httpClient);

            try (ClassicHttpResponse response = TpHttp5ClientUtils.get(URL, executor)) {
                final int statusCode = response.getCode();
                final String rawRespStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                if (log.isInfoEnabled()) {
                    log.info("code = {}", statusCode);
                    log.info("resp.length = {}", StringUtils.length(rawRespStr));
                }
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

}
