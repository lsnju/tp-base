package com.lsnju.base.http5;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;

import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http5.config.Http5Config;
import com.lsnju.base.http5.config.Http5RequestCustomizer;
import com.lsnju.base.http5.impl.DefaultTpHttp5ClientImpl;

/**
 *
 *  <blockquote><pre>
 *     try (ClassicHttpResponse response = TpHttp5ClientUtils.get(targetUrl)) {
 *          final int statusCode = response.getCode();
 *          final String rawRespStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
 *          if (log.isInfoEnabled()) {
 *              log.info("code = {}", statusCode);
 *              log.info("resp.length = {}", StringUtils.length(rawRespStr));
 *          }
 *      }
 *  </pre></blockquote>
 *
 * @author lisong
 * @since 2021/10/21 14:12
 * @version V1.0
 */
public class TpHttp5ClientUtils {

    public static final TpHttp5Client HTTP_CLIENT = newHttpClient();

    public static TpHttp5Client newHttpClient() {
        return newHttpClient(Http5Config.builder()
            .connectTimeout(DefaultTpHttp5ClientImpl.DEFAULT_CONNECT_TIMEOUT)
            .socketTimeout(DefaultTpHttp5ClientImpl.DEFAULT_SOCKET_TIMEOUT)
            .executor(Http5ExecutorUtils.defaultExecutor())
            .build());
    }

    public static TpHttp5Client newHttpClient(Http5Config config) {
        return new DefaultTpHttp5ClientImpl(config);
    }

    public static ClassicHttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse postXml(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq);
    }

    public static ClassicHttpResponse postXml(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq);
    }

    public static ClassicHttpResponse postXml(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse postXml(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse postXml(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer, executor);
    }

    public static ClassicHttpResponse postXml(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postXml(targetUrl, rawReq, customizer, executor);
    }

    public static ClassicHttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse postJson(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq);
    }

    public static ClassicHttpResponse postJson(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq);
    }

    public static ClassicHttpResponse postJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse postJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse postJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer, executor);
    }

    public static ClassicHttpResponse postJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.postJson(targetUrl, rawReq, customizer, executor);
    }

    public static ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, executor);
    }

    public static ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, executor);
    }

    public static ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, customizer, executor);
    }

    public static ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.post(targetUrl, rawReq, contentType, customizer, executor);
    }

    public static ClassicHttpResponse get(String targetUrl) throws IOException {
        return HTTP_CLIENT.get(targetUrl);
    }

    public static ClassicHttpResponse get(URI targetUrl) throws IOException {
        return HTTP_CLIENT.get(targetUrl);
    }

    public static ClassicHttpResponse get(String targetUrl, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer);
    }

    public static ClassicHttpResponse get(URI targetUrl, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer);
    }

    public static ClassicHttpResponse get(String targetUrl, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, executor);
    }

    public static ClassicHttpResponse get(URI targetUrl, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, executor);
    }

    public static ClassicHttpResponse get(String targetUrl, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer, executor);
    }

    public static ClassicHttpResponse get(URI targetUrl, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.get(targetUrl, customizer, executor);
    }

    // putJson

    public static ClassicHttpResponse putJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse putJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, executor);
    }

    public static ClassicHttpResponse putJson(String targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq);
    }

    public static ClassicHttpResponse putJson(URI targetUrl, String rawReq) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq);
    }

    public static ClassicHttpResponse putJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse putJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer);
    }

    public static ClassicHttpResponse putJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer, executor);
    }

    public static ClassicHttpResponse putJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.putJson(targetUrl, rawReq, customizer, executor);
    }

    // http xxx

    public static ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity);
    }

    public static ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Http5RequestCustomizer customizer) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, customizer);
    }

    public static ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Executor executor) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, executor);
    }

    public static ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Http5RequestCustomizer customizer, Executor executor) throws IOException {
        return HTTP_CLIENT.http(method, targetUrl, entity, customizer, executor);
    }

    // http request

    public static Request request(HttpMethod method, URI targetUrl) {
        return HTTP_CLIENT.request(method, targetUrl);
    }

}
