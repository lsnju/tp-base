package com.lsnju.base.http.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

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
        this.userAgent = Objects.toString(config.getUserAgent(), DEFAULT_USER_AGENT);
        this.socketTimeout = config.getSocketTimeout() > 0 ? config.getSocketTimeout() : DEFAULT_SOCKET_TIMEOUT;
        this.connectTimeout = config.getConnectTimeout() > 0 ? config.getConnectTimeout() : DEFAULT_CONNECT_TIMEOUT;
    }

    @Override
    public ClassicHttpResponse postXml(String targetUrl, String rawReq) throws IOException {
        return postXml(URI.create(targetUrl), rawReq);
    }

    @Override
    public ClassicHttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, customizer);
    }

    @Override
    public ClassicHttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, customizer, executor);
    }

    @Override
    public ClassicHttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException {
        return postXml(URI.create(targetUrl), rawReq, executor);
    }

    @Override
    public ClassicHttpResponse postXml(URI targetUrl, String rawReq) throws IOException {
        return postXml(targetUrl, rawReq, RequestCustomizer.NO_OP, null);
    }

    @Override
    public ClassicHttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postXml(targetUrl, rawReq, customizer, null);
    }

    @Override
    public ClassicHttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return postXml(targetUrl, rawReq, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public ClassicHttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(targetUrl, rawReq, APPLICATION_XML_UTF8, customizer, executor);
    }

    @Override
    public ClassicHttpResponse postJson(String targetUrl, String rawReq) throws IOException {
        return postJson(URI.create(targetUrl), rawReq);
    }

    @Override
    public ClassicHttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, customizer);
    }

    @Override
    public ClassicHttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, executor);
    }

    @Override
    public ClassicHttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return postJson(URI.create(targetUrl), rawReq, customizer, executor);
    }

    @Override
    public ClassicHttpResponse postJson(URI targetUrl, String rawReq) throws IOException {
        return postJson(targetUrl, rawReq, RequestCustomizer.NO_OP, null);
    }

    @Override
    public ClassicHttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException {
        return postJson(targetUrl, rawReq, customizer, null);
    }

    @Override
    public ClassicHttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException {
        return postJson(targetUrl, rawReq, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public ClassicHttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(targetUrl, rawReq, APPLICATION_JSON, customizer, executor);
    }

    @Override
    public ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return post(URI.create(targetUrl), rawReq, contentType, executor);
    }

    @Override
    public ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        return post(URI.create(targetUrl), rawReq, contentType, customizer, executor);
    }

    @Override
    public ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException {
        return post(targetUrl, rawReq, contentType, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException {
        final Request request = Request.post(targetUrl)
            .userAgent(this.userAgent)
            .connectTimeout(Timeout.ofMicroseconds(this.connectTimeout))
            .responseTimeout(Timeout.ofMicroseconds(this.socketTimeout))
            .body(new StringEntity(rawReq, contentType));
        if (customizer != null) {
            customizer.customize(request);
        }
        if (executor != null) {
            return (ClassicHttpResponse) executor.execute(request).returnResponse();
        }
        return (ClassicHttpResponse) request.execute().returnResponse();
    }

    @Override
    public ClassicHttpResponse get(String targetUrl) throws IOException {
        return get(URI.create(targetUrl));
    }

    @Override
    public ClassicHttpResponse get(String targetUrl, RequestCustomizer customizer) throws IOException {
        return get(URI.create(targetUrl), customizer);
    }

    @Override
    public ClassicHttpResponse get(String targetUrl, Executor executor) throws IOException {
        return get(URI.create(targetUrl), executor);
    }

    @Override
    public ClassicHttpResponse get(String targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        return get(URI.create(targetUrl), customizer, executor);
    }

    @Override
    public ClassicHttpResponse get(URI targetUrl) throws IOException {
        return get(targetUrl, RequestCustomizer.NO_OP, null);
    }

    @Override
    public ClassicHttpResponse get(URI targetUrl, RequestCustomizer customizer) throws IOException {
        return get(targetUrl, customizer, null);
    }


    @Override
    public ClassicHttpResponse get(URI targetUrl, Executor executor) throws IOException {
        return get(targetUrl, RequestCustomizer.NO_OP, executor);
    }

    @Override
    public ClassicHttpResponse get(URI targetUrl, RequestCustomizer customizer, Executor executor) throws IOException {
        final Request request = Request.get(targetUrl)
            .userAgent(this.userAgent)
            .connectTimeout(Timeout.ofMicroseconds(this.connectTimeout))
            .responseTimeout(Timeout.ofMicroseconds(this.socketTimeout));
        if (customizer != null) {
            customizer.customize(request);
        }
        if (executor != null) {
            return (ClassicHttpResponse) executor.execute(request).returnResponse();
        }
        return (ClassicHttpResponse) request.execute().returnResponse();
    }
}
