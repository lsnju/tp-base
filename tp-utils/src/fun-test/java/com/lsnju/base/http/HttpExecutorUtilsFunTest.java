package com.lsnju.base.http;

import java.nio.charset.StandardCharsets;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;

import com.lsnju.base.http5.Http5ExecutorUtils;
import com.lsnju.base.http5.TpHttp5ClientUtils;
import com.lsnju.base.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2025-04-25 23:43:04
 * @version V1.0
 */
@Slf4j
public class HttpExecutorUtilsFunTest {

    String url = "https://117.184.192.242:9443/api/hmd/hmdMicpPay/v2";

    @Test
    void test_001() {
        try {
            final ClassicHttpResponse returnClassicHttpResponse = TpHttp5ClientUtils.get(url, Http5ExecutorUtils.newTrustAllInstance());
            final int statusCode = returnClassicHttpResponse.getCode();
            final String rawResp = EntityUtils.toString(returnClassicHttpResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }
            log.info("{}", JsonUtils.toPrettyFormat(rawResp));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_002() {
        try {
            final ClassicHttpResponse returnClassicHttpResponse = TpHttp5ClientUtils.get(url, Http5ExecutorUtils.newTrustAllInstance2());
            final int statusCode = returnClassicHttpResponse.getCode();
            final String rawResp = EntityUtils.toString(returnClassicHttpResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }
            log.info("{}", JsonUtils.toPrettyFormat(rawResp));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_003() {
        try {
            final ClassicHttpResponse returnClassicHttpResponse = TpHttp5ClientUtils.get(url, Http5ExecutorUtils.TRUST_ALL_EXECUTOR);
            final int statusCode = returnClassicHttpResponse.getCode();
            final String rawResp = EntityUtils.toString(returnClassicHttpResponse.getEntity(), StandardCharsets.UTF_8);
            if (log.isInfoEnabled()) {
                log.info("code = {}, rawResp = {}", statusCode, rawResp);
            }
            log.info("{}", JsonUtils.toPrettyFormat(rawResp));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

}
