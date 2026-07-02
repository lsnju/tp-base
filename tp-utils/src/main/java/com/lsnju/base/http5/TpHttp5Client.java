package com.lsnju.base.http5;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;

import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http5.config.Http5RequestCustomizer;

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
 * @since 2021/11/5 11:18
 * @version V1.0
 */
public interface TpHttp5Client {

    // postXml

    ClassicHttpResponse postXml(String targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse postXml(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse postXml(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    ClassicHttpResponse postXml(URI targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse postXml(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse postXml(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // postJson

    ClassicHttpResponse postJson(String targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse postJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse postJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    ClassicHttpResponse postJson(URI targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse postJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse postJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // post

    ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException;

    ClassicHttpResponse post(String targetUrl, String rawReq, ContentType contentType, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException;

    ClassicHttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // get

    ClassicHttpResponse get(String targetUrl) throws IOException;

    ClassicHttpResponse get(String targetUrl, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse get(String targetUrl, Executor executor) throws IOException;

    ClassicHttpResponse get(String targetUrl, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    ClassicHttpResponse get(URI targetUrl) throws IOException;

    ClassicHttpResponse get(URI targetUrl, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse get(URI targetUrl, Executor executor) throws IOException;

    ClassicHttpResponse get(URI targetUrl, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // putJson

    ClassicHttpResponse putJson(String targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse putJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse putJson(String targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse putJson(String targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    ClassicHttpResponse putJson(URI targetUrl, String rawReq) throws IOException;

    ClassicHttpResponse putJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse putJson(URI targetUrl, String rawReq, Executor executor) throws IOException;

    ClassicHttpResponse putJson(URI targetUrl, String rawReq, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // http xxx

    ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity) throws IOException;

    ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Http5RequestCustomizer customizer) throws IOException;

    ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Executor executor) throws IOException;

    ClassicHttpResponse http(HttpMethod method, URI targetUrl, HttpEntity entity, Http5RequestCustomizer customizer, Executor executor) throws IOException;

    // http

    Request request(HttpMethod method, URI targetUrl);

}
