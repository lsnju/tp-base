package com.lsnju.base.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.lsnju.base.http.config.HttpConfig;
import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http.config.RequestCustomizer;
import com.lsnju.base.http.impl.DefaultTpHttpClientImpl;

/**
 *
 *  <blockquote><pre>
 *      final HttpResponse returnResponse = CLIENT.get(targetUrl);
 *      final int statusCode = returnResponse.getStatusLine().getStatusCode();
 *      final String rawResp = EntityUtils.toString(returnResponse.getEntity(), StandardCharsets.UTF_8);
 *      if (log.isInfoEnabled()) {
 *          log.info("code = {}, rawResp = {}", statusCode, rawResp);
 *      }
 *  </pre></blockquote>
 *
 * @author lisong
 * @since 2021/10/21 14:12
 * @version V1.0
 */
public class TpHttpClientUtils {

    public static final TpHttpClient HTTP_CLIENT = newHttpClient();

    public static TpHttpClient newHttpClient() {
        return newHttpClient(HttpConfig.builder()
            .connectTimeout(DefaultTpHttpClientImpl.DEFAULT_CONNECT_TIMEOUT)
            .socketTimeout(DefaultTpHttpClientImpl.DEFAULT_SOCKET_TIMEOUT)
            .executor(HttpExecutorUtils.defaultExecutor())
            .build());
    }

    public static TpHttpClient newHttpClient(HttpConfig config) {
        return new DefaultTpHttpClientImpl(config);
    }

    public static HttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, executor);
    }

    public static HttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, executor);
    }

    public static HttpResponse postXml(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq);
    }

    public static HttpResponse postXml(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq);
    }

    public static HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer);
    }

    public static HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer);
    }

    public static HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer, executor);
    }

    public static HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer, executor);
    }

    public static HttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, executor);
    }

    public static HttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, executor);
    }

    public static HttpResponse postJson(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq);
    }

    public static HttpResponse postJson(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq);
    }

    public static HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer);
    }

    public static HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer);
    }

    public static HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer, executor);
    }

    public static HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer, executor);
    }

    public static HttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, executor);
    }

    public static HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, executor);
    }

    public static HttpResponse post(String targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, customizer, executor);
    }

    public static HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, customizer, executor);
    }

    public static HttpResponse get(String targetUrl) throws IOException {
        return HTTP_CLIENT.get(targetUrl);
    }

    public static HttpResponse get(URI targetUrl) throws IOException {
        return HTTP_CLIENT.get(targetUrl);
    }

    public static HttpResponse get(String targetUrl, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer);
    }

    public static HttpResponse get(URI targetUrl, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer);
    }

    public static HttpResponse get(String targetUrl, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, executor);
    }

    public static HttpResponse get(URI targetUrl, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, executor);
    }

    public static HttpResponse get(String targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer, executor);
    }

    public static HttpResponse get(URI targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer, executor);
    }

    // putJson

    public static HttpResponse putJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, executor);
    }

    public static HttpResponse putJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, executor);
    }

    public static HttpResponse putJson(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq);
    }

    public static HttpResponse putJson(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq);
    }

    public static HttpResponse putJson(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer);
    }

    public static HttpResponse putJson(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer);
    }

    public static HttpResponse putJson(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer, executor);
    }

    public static HttpResponse putJson(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer, executor);
    }

    // http xxx

    public static HttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity);
    }

    public static HttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, customizer);
    }

    public static HttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Executor executor) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, executor);
    }

    public static HttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, customizer, executor);
    }

    // http request

    public static Request request(HttpMethod method, URI targetUrl) {
        return HTTP_CLIENT.request(method, targetUrl);
    }

}
