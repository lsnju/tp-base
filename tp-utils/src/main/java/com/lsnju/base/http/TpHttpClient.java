package com.lsnju.base.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.entity.ContentType;

import com.lsnju.base.http.config.RequestCustomizer;

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
 * @since 2021/11/5 11:18
 * @version V1.0
 */
public interface TpHttpClient {

    // postXml

    HttpResponse postXml(String targetUrl, String rawReq) throws IOException;

    HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException;

    HttpResponse postXml(String targetUrl, String rawReq, Executor executor) throws IOException;

    HttpResponse postXml(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException;

    HttpResponse postXml(URI targetUrl, String rawReq) throws IOException;

    HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException;

    HttpResponse postXml(URI targetUrl, String rawReq, Executor executor) throws IOException;

    HttpResponse postXml(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException;

    // postJson

    HttpResponse postJson(String targetUrl, String rawReq) throws IOException;

    HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer) throws IOException;

    HttpResponse postJson(String targetUrl, String rawReq, Executor executor) throws IOException;

    HttpResponse postJson(String targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException;

    HttpResponse postJson(URI targetUrl, String rawReq) throws IOException;

    HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer) throws IOException;

    HttpResponse postJson(URI targetUrl, String rawReq, Executor executor) throws IOException;

    HttpResponse postJson(URI targetUrl, String rawReq, RequestCustomizer customizer, Executor executor) throws IOException;

    // post

    HttpResponse post(String targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException;

    HttpResponse post(String targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException;

    HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, Executor executor) throws IOException;

    HttpResponse post(URI targetUrl, String rawReq, ContentType contentType, RequestCustomizer customizer, Executor executor) throws IOException;

    // get

    HttpResponse get(String targetUrl) throws IOException;

    HttpResponse get(String targetUrl, RequestCustomizer customizer) throws IOException;

    HttpResponse get(String targetUrl, Executor executor) throws IOException;

    HttpResponse get(String targetUrl, RequestCustomizer customizer, Executor executor) throws IOException;

    HttpResponse get(URI targetUrl) throws IOException;

    HttpResponse get(URI targetUrl, RequestCustomizer customizer) throws IOException;

    HttpResponse get(URI targetUrl, Executor executor) throws IOException;

    HttpResponse get(URI targetUrl, RequestCustomizer customizer, Executor executor) throws IOException;

}
