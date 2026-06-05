package com.lsnju.base.http5;

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

import com.lsnju.base.http.config.HttpMethod;
import com.lsnju.base.http5.config.Http5Config;
import com.lsnju.base.http5.impl.DefaultTpHttp5ClientImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class TpHttp5ClientUtilsTest {

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
        Assertions.assertNotNull(TpHttp5ClientUtils.HTTP_CLIENT);
        Assertions.assertTrue(TpHttp5ClientUtils.HTTP_CLIENT instanceof DefaultTpHttp5ClientImpl);
        Assertions.assertTrue(TpHttp5ClientUtils.newHttpClient() instanceof DefaultTpHttp5ClientImpl);
    }

    @Test
    void newHttpClient_withCustomConfig_shouldUseConfigValues() throws Exception {
        Http5Config config = Http5Config.builder()
            .userAgent("tp-test-agent")
            .connectTimeout(3210)
            .socketTimeout(6540)
            .build();

        TpHttp5Client client = TpHttp5ClientUtils.newHttpClient(config);
        Assertions.assertTrue(client instanceof DefaultTpHttp5ClientImpl);

        DefaultTpHttp5ClientImpl impl = (DefaultTpHttp5ClientImpl) client;
        Assertions.assertEquals("tp-test-agent", readPrivateField(impl, "userAgent"));
        Assertions.assertEquals(3210, readPrivateField(impl, "connectTimeout"));
        Assertions.assertEquals(6540, readPrivateField(impl, "socketTimeout"));
    }

    @Test
    void get_withStringAndUri_shouldReturn200() throws Exception {
        String targetUrl = baseUri.resolve("/echo").toString();
        try (ClassicHttpResponse response1 = TpHttp5ClientUtils.get(targetUrl)) {
            Assertions.assertEquals(200, response1.getCode());
            Assertions.assertTrue(EntityUtils.toString(response1.getEntity(), StandardCharsets.UTF_8).contains("GET|"));
        }

        try (ClassicHttpResponse response2 = TpHttp5ClientUtils.get(URI.create(targetUrl))) {
            Assertions.assertEquals(200, response2.getCode());
            Assertions.assertTrue(EntityUtils.toString(response2.getEntity(), StandardCharsets.UTF_8).contains("GET|"));
        }
    }

    @Test
    void postJson_andPutJson_shouldSendBody() throws Exception {
        String targetUrl = baseUri.resolve("/echo").toString();

        try (ClassicHttpResponse postResp = TpHttp5ClientUtils.postJson(targetUrl, "{\"name\":\"tp\"}")) {
            String body = EntityUtils.toString(postResp.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertEquals(200, postResp.getCode());
            Assertions.assertTrue(body.contains("POST|{\"name\":\"tp\"}"));
        }

        try (ClassicHttpResponse putResp = TpHttp5ClientUtils.putJson(URI.create(targetUrl), "{\"id\":1}")) {
            String body = EntityUtils.toString(putResp.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertEquals(200, putResp.getCode());
            Assertions.assertTrue(body.contains("PUT|{\"id\":1}"));
        }
    }

    @Test
    void http_shouldSendMethodAndBody() throws Exception {
        URI targetUrl = baseUri.resolve("/echo");
        StringEntity entity = new StringEntity("tp-body", ContentType.TEXT_PLAIN);
        try (ClassicHttpResponse response = TpHttp5ClientUtils.http(HttpMethod.POST, targetUrl, entity)) {
            Assertions.assertEquals(200, response.getCode());
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Assertions.assertTrue(body.contains("POST|tp-body"));
        }
    }

    @Test
    void request_connect_shouldThrow() {
        URI targetUrl = baseUri.resolve("/echo");
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
            () -> TpHttp5ClientUtils.request(HttpMethod.CONNECT, targetUrl));
        Assertions.assertTrue(ex.getMessage().contains("unknown method"));
    }

    private static Object readPrivateField(DefaultTpHttp5ClientImpl impl, String fieldName) throws Exception {
        Field field = DefaultTpHttp5ClientImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(impl);
    }

    private static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] raw = exchange.getRequestBody().readAllBytes();
            String reqBody = new String(raw, StandardCharsets.UTF_8);
            String resp = exchange.getRequestMethod() + "|" + reqBody;
            byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }
}
