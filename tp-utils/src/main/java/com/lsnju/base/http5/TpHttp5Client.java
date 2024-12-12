package com.lsnju.base.http5;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;

import com.lsnju.base.http5.config.Http5RequestCustomizer;

/**
 *
 *  <blockquote><pre>
 *      final ClassicHttpResponse response = CLIENT.get(targetUrl);
 *      final int statusCode = response.getCode();
 *      final String rawResp = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
 *      if (log.isInfoEnabled()) {
 *          log.info("code = {}, rawResp = {}", statusCode, rawResp);
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

}
