package com.lsnju.base.http.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.lsnju.base.http.TpHttpClient;
import com.lsnju.base.http.config.HttpConfig;
import com.lsnju.base.http.config.RequestCustomizer;
import com.lsnju.base.util.TpAppInfo;

/**
 *
 * @author lisong
 * @since 2021/11/5 11:03
 * @version V1.0
 */
public class DefaultTpHttpClientImpl implements TpHttpClient {

    public static final ContentType APPLICATION_XML_UTF8 = ContentType.create(ContentType.APPLICATION_XML.getMimeType(), StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_JSON = ContentType.APPLICATION_JSON;

    public final String DEFAULT_USER_AGENT = "Tp/" + TpAppInfo.TP_BASE_VERSION + " (Java/" + TpAppInfo.JAVA_VERSION + ")";
    public static final int DEFAULT_CONNECT_TIMEOUT = 10_000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 20_000;

    private final String userAgent;
    private final int socketTimeout;
    private final int connectTimeout;

    public DefaultTpHttpClientImpl(HttpConfig config) {
        this.userAgent = StringUtils.defaultString(config.getUserAgent(), DEFAULT_USER_AGENT);
        this.socketTimeout = config.getSocketTimeout() > 0 ? config.getSocketTimeout() : DEFAULT_SOCKET_TIMEOUT;
        this.connectTimeout = config.getConnectTimeout() > 0 ? config.getConnectTimeout() : DEFAULT_CONNECT_TIMEOUT;
    }

    @Override
    public HttpResponse postXml(String targetUrl, String rawReq) throws IOException {
        return postXml(URI.create(targetUrl), rawReq);
    }

    @Override
    public HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, customizer);
    }

    @Override
    public HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, customizer, executor);
    }

    @Override
    public HttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, executor);
    }

    @Override
    public HttpResponse postXml(URI targetUrl, String rawReq) throws IOException {
        return postXml(targetUrl, rawReq, RequestCustomizer.NO_OP, null);
    }

    @Override
    public HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postXml(targetUrl, rawReq, customizer, null);
    }

    @Override
    public HttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return postXml(targetUrl, rawReq, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(targetUrl, rawReq, APPLICATION_XML_UTF8, customizer, executor);
    }

    @Override
    public HttpResponse postJson(String targetUrl, String rawReq) throws IOException {
        return postJson(URI.create(targetUrl), rawReq);
    }

    @Override
    public HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, customizer);
    }

    @Override
    public HttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, executor);
    }

    @Override
    public HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, customizer, executor);
    }

    @Override
    public HttpResponse postJson(URI targetUrl, String rawReq) throws IOException {
        return postJson(targetUrl, rawReq, RequestCustomizer.NO_OP, null);
    }

    @Override
    public HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postJson(targetUrl, rawReq, customizer, null);
    }

    @Override
    public HttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return postJson(targetUrl, rawReq, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(targetUrl, rawReq, APPLICATION_JSON, customizer, executor);
    }

    @Override
    public HttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return post(URI.create(targetUrl), rawReq, contentType, executor);
    }

    @Override
    public HttpResponse post(String targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(URI.create(targetUrl), rawReq, contentType, customizer, executor);
    }

    @Override
    public HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return post(targetUrl, rawReq, contentType, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        final Request request = Request.Post(targetUrl)
            .userAgent(this.userAgent)
            .connectTimeout(this.connectTimeout)
            .socketTimeout(this.socketTimeout)
            .body(new StringEntity(rawReq, contentType));
        if (customizer != null) {
            customizer.customize(request);
        }
        if (executor != null) {
            return executor.execute(request).returnResponse();
        }
        return request.execute().returnResponse();
    }

    @Override
    public HttpResponse get(String targetUrl) throws IOException {
        return get(URI.create(targetUrl));
    }

    @Override
    public HttpResponse get(String targetUrl, RequestCustomizer customizer) throws IOException {
        return get(URI.create(targetUrl), customizer);
    }

    @Override
    public HttpResponse get(String targetUrl, Executor executor) throws IOException {
        return get(URI.create(targetUrl), executor);
    }

    @Override
    public HttpResponse get(String targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        return get(URI.create(targetUrl), customizer, executor);
    }

    @Override
    public HttpResponse get(URI targetUrl) throws IOException {
        return get(targetUrl, RequestCustomizer.NO_OP, null);
    }

    @Override
    public HttpResponse get(URI targetUrl, RequestCustomizer customizer) throws IOException {
        return get(targetUrl, customizer, null);
    }


    @Override
    public HttpResponse get(URI targetUrl, Executor executor) throws IOException {
        return get(targetUrl, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public HttpResponse get(URI targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        final Request request = Request.Get(targetUrl)
            .userAgent(this.userAgent)
            .connectTimeout(this.connectTimeout)
            .socketTimeout(this.socketTimeout);
        if (customizer != null) {
            customizer.customize(request);
        }
        if (executor != null) {
            return executor.execute(request).returnResponse();
        }
        return request.execute().returnResponse();
    }
}
