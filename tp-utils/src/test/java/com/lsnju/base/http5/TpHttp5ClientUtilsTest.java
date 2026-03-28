package com.lsnju.base.http5;

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

import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lsnju.base.http.config.HttpConfig;
import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http5.impl.DefaultTpHttp5ClientImpl;
import com.sun.net.httpserver.HttpServer;

/**
 * Tests for {@link TpHttp5ClientUtils} against a local {@link HttpServer}.
 *
 * @author ls
 */
class TpHttp5ClientUtilsTest {

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

    private static String body(ClassicHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        try {
            if (entity == null) {
                return "";
            }
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            throw new IOException(e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    private static Executor testExecutor() {
        return Executor.newInstance(HttpClients.createDefault());
    }

    @Test
    void httpClient_singleton_and_newClient() {
        assertNotNull(TpHttp5ClientUtils.HTTP_CLIENT);
        TpHttp5Client c1 = TpHttp5ClientUtils.newHttpClient();
        assertNotNull(c1);
        assertTrue(c1 instanceof DefaultTpHttp5ClientImpl);
        TpHttp5Client c2 = TpHttp5ClientUtils.newHttpClient(HttpConfig.builder().build());
        assertNotNull(c2);
    }

    @Test
    void get_string_and_uri() throws IOException {
        String url = baseUrl + "/api";
        assertTrue(body(TpHttp5ClientUtils.get(url)).startsWith("GET\n"));
        assertTrue(body(TpHttp5ClientUtils.get(URI.create(url))).startsWith("GET\n"));
    }

    @Test
    void get_withCustomizer_and_executor() throws IOException {
        String url = baseUrl + "/api";
        String r1 = body(TpHttp5ClientUtils.get(url, req -> req.addHeader("X-Test", "c1")));
        assertTrue(r1.contains("c1"));
        Executor ex = testExecutor();
        String r2 = body(TpHttp5ClientUtils.get(url, ex));
        assertTrue(r2.startsWith("GET\n"));
        String r3 = body(TpHttp5ClientUtils.get(url, req -> req.addHeader("X-Test", "c2"), ex));
        assertTrue(r3.contains("c2"));
    }

    @Test
    void postJson_string_and_uri() throws IOException {
        String url = baseUrl + "/api";
        String json = "{\"a\":1}";
        String r = body(TpHttp5ClientUtils.postJson(url, json));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("application/json"));
        assertTrue(r.contains(json));
        assertTrue(body(TpHttp5ClientUtils.postJson(URI.create(url), json)).contains(json));
    }

    @Test
    void postJson_customizer_executor() throws IOException {
        String url = baseUrl + "/api";
        Executor ex = testExecutor();
        String r = body(TpHttp5ClientUtils.postJson(url, "{}", req -> req.addHeader("X-Test", "pj"), ex));
        assertTrue(r.contains("pj"));
    }

    @Test
    void postXml() throws IOException {
        String url = baseUrl + "/api";
        String xml = "<r/>";
        String r = body(TpHttp5ClientUtils.postXml(url, xml));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("application/xml"));
        assertTrue(r.contains(xml));
    }

    @Test
    void post_contentType() throws IOException {
        String url = baseUrl + "/api";
        String r = body(TpHttp5ClientUtils.post(url, "hi", ContentType.TEXT_PLAIN, testExecutor()));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("text/plain"));
        assertTrue(r.contains("hi"));
    }

    @Test
    void putJson() throws IOException {
        String url = baseUrl + "/api";
        String json = "{\"b\":2}";
        String r = body(TpHttp5ClientUtils.putJson(url, json));
        assertTrue(r.startsWith("PUT\n"));
        assertTrue(r.contains("application/json"));
        assertTrue(r.contains(json));
    }

    @Test
    void http_delete_noEntity() throws IOException {
        String url = baseUrl + "/api";
        String r = body(TpHttp5ClientUtils.http(HttpMethod.DELETE, URI.create(url), null));
        assertTrue(r.startsWith("DELETE\n"));
    }

    @Test
    void http_post_withEntity() throws IOException {
        String url = baseUrl + "/api";
        HttpEntity entity = new StringEntity("body", ContentType.APPLICATION_JSON);
        String r = body(TpHttp5ClientUtils.http(HttpMethod.POST, URI.create(url), entity));
        assertTrue(r.startsWith("POST\n"));
        assertTrue(r.contains("body"));
    }

    @Test
    void request_buildsFluentRequest() {
        URI uri = URI.create(baseUrl + "/api");
        assertNotNull(TpHttp5ClientUtils.request(HttpMethod.GET, uri));
        assertNotNull(TpHttp5ClientUtils.request(HttpMethod.POST, uri));
    }

    @Test
    void request_connect_throws() {
        URI uri = URI.create(baseUrl + "/api");
        assertThrows(RuntimeException.class, () -> TpHttp5ClientUtils.request(HttpMethod.CONNECT, uri));
    }

    @Test
    void newHttpClient_customUserAgent() throws IOException {
        TpHttp5Client client = TpHttp5ClientUtils.newHttpClient(
            HttpConfig.builder().userAgent("UNIT-TEST-UA-5").build());
        String ua = body(client.get(baseUrl + "/ua"));
        assertEquals("UNIT-TEST-UA-5", ua);
    }
}
