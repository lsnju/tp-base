package com.lsnju.base.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lsnju.base.http.config.HttpConfig;
import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http.impl.DefaultTpHttpClientImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class TpHttpClientUtilsTest {

    private HttpServer server;
    private URI baseUri;

    @BeforeEach
    void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/echo", new EchoHandler());
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
        baseUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void httpClient_andFactory_shouldReturnDefaultImpl() {
        Assertions.assertNotNull(TpHttpClientUtils.HTTP_CLIENT);
        Assertions.assertTrue(TpHttpClientUtils.HTTP_CLIENT instanceof DefaultTpHttpClientImpl);
        Assertions.assertTrue(TpHttpClientUtils.newHttpClient() instanceof DefaultTpHttpClientImpl);
    }

    @Test
    void newHttpClient_withCustomConfig_shouldUseConfigValues() throws Exception {
        HttpConfig config = HttpConfig.builder()
            .userAgent("tp-test-agent")
            .connectTimeout(4321)
            .socketTimeout(8765)
            .build();

        TpHttpClient client = TpHttpClientUtils.newHttpClient(config);
        Assertions.assertTrue(client instanceof DefaultTpHttpClientImpl);

        DefaultTpHttpClientImpl impl = (DefaultTpHttpClientImpl) client;
        Assertions.assertEquals("tp-test-agent", readPrivateField(impl, "userAgent"));
        Assertions.assertEquals(4321, readPrivateField(impl, "connectTimeout"));
        Assertions.assertEquals(8765, readPrivateField(impl, "socketTimeout"));
    }

    @Test
    void get_withStringAndUri_shouldReturn200() throws Exception {
        String targetUrl = baseUri.resolve("/echo").toString();
        try (ClassicHttpResponse response1 = TpHttpClientUtils.get(targetUrl)) {
            Assertions.assertEquals(200, response1.getCode());
            Assertions.assertTrue(EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8).startsWith("GET|"));
        }

        try (ClassicHttpResponse response2 = TpHttpClientUtils.get(URI.create(targetUrl))) {
            Assertions.assertEquals(200, response2.getCode());
            Assertions.assertTrue(EntityUtils.toString(response2.getEntity(), StandardCharsets.UTF_8).startsWith("GET|"));
        }
    }

    @Test
    void postJson_andPutJson_shouldSendBody() throws Exception {
        String targetUrl = baseUri.resolve("/echo").toString();

        try (ClassicHttpResponse postResp = TpHttpClientUtils.postJson(targetUrl, "{\"name\":\"tp\"}")) {
            String body = EntityUtils.toString(postResp.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertEquals(200, postResp.getCode());
            Assertions.assertTrue(body.contains("POST|{\"name\":\"tp\"}"));
        }

        try (ClassicHttpResponse putResp = TpHttpClientUtils.putJson(URI.create(targetUrl), "{\"id\":1}")) {
            String body = EntityUtils.toString(putResp.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertEquals(200, putResp.getCode());
            Assertions.assertTrue(body.contains("PUT|{\"id\":1}"));
        }
    }

    @Test
    void get_withCustomizer_shouldApplyHeader() throws Exception {
        String targetUrl = baseUri.resolve("/echo").toString();
        try (ClassicHttpResponse response = TpHttpClientUtils.get(targetUrl,
            request -> request.addHeader("X-Test-Flag", "customized"))) {
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertEquals(200, response.getCode());
            Assertions.assertTrue(body.endsWith("|customized"));
        }
    }

    @Test
    void http_andRequest_connectShouldThrow() throws Exception {
        URI targetUrl = baseUri.resolve("/echo");
        StringEntity entity = new StringEntity("tp-body", ContentType.TEXT_PLAIN);

        try (ClassicHttpResponse response = TpHttpClientUtils.http(HttpMethod.POST, targetUrl, entity)) {
            Assertions.assertEquals(200, response.getCode());
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertTrue(body.contains("POST|tp-body"));
        }

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
            () -> TpHttpClientUtils.request(HttpMethod.CONNECT, targetUrl));
        Assertions.assertTrue(ex.getMessage().contains("unknown method"));
    }

    private static Object readPrivateField(DefaultTpHttpClientImpl impl, String fieldName) throws Exception {
        Field field = DefaultTpHttpClientImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(impl);
    }

    private static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] raw = exchange.getRequestBody().readAllBytes();
            String reqBody = new String(raw, StandardCharsets.UTF_8);
            String customHeader = exchange.getRequestHeaders().getFirst("X-Test-Flag");
            String resp = exchange.getRequestMethod() + "|" + reqBody + "|" + customHeader;
            byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }
}
