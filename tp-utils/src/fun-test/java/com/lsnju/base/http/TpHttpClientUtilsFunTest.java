package com.lsnju.base.http;

import java.nio.charset.StandardCharsets;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import com.lsnju.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/1/10 10:13
 * @version V1.0
 */
@Slf4j
public class TpHttpClientUtilsFunTest {

    public static final String URL = "http://localhost:8080/tp/mo/sysinfo.json";

    @Test
    void test_001() {
        try {
            HttpResponse returnResponse = TpHttpClientUtils.get(URL);
            final int statusCode = returnResponse.getStatusLine().getStatusCode();
            final String rawResp = EntityUtils.toString(returnResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_002() {
        try {
            final Request request = Request.Get(URL);
            HttpResponse returnResponse = request.execute().returnResponse();

            final int statusCode = returnResponse.getStatusLine().getStatusCode();
            final String rawResp = EntityUtils.toString(returnResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_003() {

        // Creating an HttpRequestInterceptor
        HttpRequestInterceptor requestInterceptor = (request, context) -> {
            log.info("----------------------");
            log.info("request = {}", request);
            log.info("context = {}", context);
            log.info("headers = {}", JsonUtils.toJson(request.getAllHeaders()));
            log.info("----------------------");
        };

        CloseableHttpClient httpclient = HttpClients.custom().addInterceptorFirst(requestInterceptor).build();
        Executor executor = Executor.newInstance(httpclient);

        try {
            final Request request = Request.Get(URL);
            HttpResponse returnResponse = executor.execute(request).returnResponse();

            final int statusCode = returnResponse.getStatusLine().getStatusCode();
            final String rawResp = EntityUtils.toString(returnResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }

        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }
}
