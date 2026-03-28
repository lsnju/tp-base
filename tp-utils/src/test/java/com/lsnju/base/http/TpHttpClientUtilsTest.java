package com.lsnju.base.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lsnju.base.http.config.HttpConfig;
import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http.impl.DefaultTpHttpClientImpl;
import com.sun.net.httpserver.HttpServer;

/**
 * Tests for {@link TpHttpClientUtils} against a local {@link HttpServer}.
 *
 * @author ls
 */
class TpHttpClientUtilsTest {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/api", exchange -> {
            try {
                String method = exchange.getRequestMethod();
                String ct = exchange.getRequestHeaders().getFirst("Content-Type");
                String xTest = exchange.getRequestHeaders().getFirst("X-Test");
                byte[] in = readAll(exchange.getRequestBody());
                String inStr = new String(in, StandardCharsets.UTF_8);
                String payload = method + "\n" + String.valueOf(ct) + "\n" + String.valueOf(xTest) + "\n" + inStr;
                byte[] out = payload.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, out.length);
                exchange.getResponseBody().write(out);
            } finally {
                exchange.close();
            }
        });
        server.createContext("/ua", exchange -> {
            try {
                String ua = exchange.getRequestHeaders().getFirst("User-Agent");
                byte[] out = (ua != null ? ua : "").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, out.length);
                exchange.getResponseBody().write(out);
            } finally {
                exchange.close();
            }
        });
        server.setExecutor(null);
        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://127.0.0.1:" + port;
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    private static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        int n;
        while ((n = is.read(b)) != -1) {
            buf.write(b, 0, n);
        }
        return buf.toByteArray();
    }

    private static String body(HttpResponse response) throws IOException {
        try {
            if (response.getEntity() == null) {
                return "";
            }
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } finally {
            EntityUtils.consumeQuietly(response.getEntity());
        }
    }

    @Test
    void httpClient_singleton_and_newClient() {
        assertNotNull(TpHttpClientUtils.HTTP_CLIENT);
        TpHttpClient c1 = TpHttpClientUtils.newHttpClient();
        assertNotNull(c1);
        assertTrue(c1 instanceof DefaultTpHttpClientImpl);
        TpHttpClient c2 = TpHttpClientUtils.newHttpClient(HttpConfig.builder().build());
        assertNotNull(c2);
    }

    @Test
    void get_string_and_uri() throws IOException {
        String url = baseUrl + "/api";
        assertTrue(body(TpHttpClientUtils.get(url)).startsWith("GET\n"));
        assertTrue(body(TpHttpClientUtils.get(URI.create(url))).startsWith("GET\n"));
    }

    @Test
    void get_withCustomizer_and_executor() throws IOException {
        String url = baseUrl + "/api";
        String r1 = body(TpHttpClientUtils.get(url, req -> req.addHeader("X-Test", "c1")));
        assertTrue(r1.contains("c1"));
        Executor ex = Executor.newInstance(HttpClients.createDefault());
        String r2 = body(TpHttpClientUtils.get(url, ex));
        assertTrue(r2.startsWith("GET\n"));
        String r3 = body(TpHttpClientUtils.get(url, req -> req.addHeader("X-Test", "c2"), ex));
        assertTrue(r3.contains("c2"));
    }

    @Test
    void postJson_string_and_uri() throws IOException {
        String url = baseUrl + "/api";
        String json = "{\"a\":1}";
        String r = body(TpHttpClientUtils.postJson(url, json));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("application/json"));
        assertTrue(r.contains(json));
        assertTrue(body(TpHttpClientUtils.postJson(URI.create(url), json)).contains(json));
    }

    @Test
    void postJson_customizer_executor() throws IOException {
        String url = baseUrl + "/api";
        Executor ex = Executor.newInstance(HttpClients.createDefault());
        String r = body(TpHttpClientUtils.postJson(url, "{}", req -> req.addHeader("X-Test", "pj"), ex));
        assertTrue(r.contains("pj"));
    }

    @Test
    void postXml() throws IOException {
        String url = baseUrl + "/api";
        String xml = "<r/>";
        String r = body(TpHttpClientUtils.postXml(url, xml));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("application/xml"));
        assertTrue(r.contains(xml));
    }

    @Test
    void post_contentType() throws IOException {
        String url = baseUrl + "/api";
        String r = body(TpHttpClientUtils.post(url, "hi", ContentType.TEXT_PLAIN,
            Executor.newInstance(HttpClients.createDefault())));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("text/plain"));
        assertTrue(r.contains("hi"));
    }

    @Test
    void putJson() throws IOException {
        String url = baseUrl + "/api";
        String json = "{\"b\":2}";
        String r = body(TpHttpClientUtils.putJson(url, json));
        assertTrue(r.startsWith("PUT\n"));
        assertTrue(r.contains("application/json"));
        assertTrue(r.contains(json));
    }

    @Test
    void http_delete_noEntity() throws IOException {
        String url = baseUrl + "/api";
        String r = body(TpHttpClientUtils.http(HttpMethod.DELETE, URI.create(url), null));
        assertTrue(r.startsWith("DELETE\n"));
    }

    @Test
    void http_post_withEntity() throws IOException {
        String url = baseUrl + "/api";
        HttpEntity entity = new StringEntity("body", ContentType.APPLICATION_JSON);
        String r = body(TpHttpClientUtils.http(HttpMethod.POST, URI.create(url), entity));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("body"));
    }

    @Test
    void request_buildsFluentRequest() {
        URI uri = URI.create(baseUrl + "/api");
        assertNotNull(TpHttpClientUtils.request(HttpMethod.GET, uri));
        assertNotNull(TpHttpClientUtils.request(HttpMethod.POST, uri));
    }

    @Test
    void request_connect_throws() {
        URI uri = URI.create(baseUrl + "/api");
        assertThrows(RuntimeException.class, () -> TpHttpClientUtils.request(HttpMethod.CONNECT, uri));
    }

    @Test
    void newHttpClient_customUserAgent() throws IOException {
        TpHttpClient client = TpHttpClientUtils.newHttpClient(
            HttpConfig.builder().userAgent("UNIT-TEST-UA").build());
        String ua = body(client.get(baseUrl + "/ua"));
        assertEquals("UNIT-TEST-UA", ua);
    }
}
