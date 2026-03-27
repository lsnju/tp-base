package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import com.lsnju.base.gson.GsonUtils;
import com.lsnju.base.util.vo.DateTimeBean;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2024/11/2 12:36
 * @version V1.0
 */
@Slf4j
public class GsonUtilsFunTest {

    @Test
    void test_001() {
        String jsonStr = "{\n" +
            "  \"name\" : \"name\",\n" +
            "  \"zonedDateTime\" : \"2024-11-02T12:35:22.551+08:00\",\n" +
            "  \"localDateTime\" : \"2024-11-02T12:35:22.551\",\n" +
            "  \"date\" : \"2024-11-02T12:35:22.552+0800\"\n" +
            "}";
        final DateTimeBean ret = GsonUtils.fromJson(jsonStr, DateTimeBean.class);
        log.info("{}", ret);
        log.info("{}", GsonUtils.toJsonPretty(ret));
    }

    @Test
    void test_002() {
        String jsonStr = "{\n" +
            "  \"name\" : \"name\",\n" +
            "  \"zonedDateTime\" : \"2024-11-02T12:35:22+08:00\",\n" +
            "  \"localDateTime\" : \"2024-11-02T12:35:22\",\n" +
            "  \"date\" : \"2024-11-02T12:35:22+0800\"\n" +
            "}";
        final DateTimeBean ret = GsonUtils.fromJson(jsonStr, DateTimeBean.class);
        log.info("{}", ret);
        log.info("{}", GsonUtils.toJsonPretty(ret));
    }

    @Test
    void test_003() {
        String jsonStr = "{\n" +
            "  \"name\" : \"name\",\n" +
            "  \"zonedDateTime\" : \"2024-11-02T12:35+08:00\",\n" +
            "  \"localDateTime\" : \"2024-11-02T12:35\",\n" +
            "  \"date\" : \"2024-11-02T12:35+08:00\"\n" +
            "}";
        final DateTimeBean ret = GsonUtils.fromJson(jsonStr, DateTimeBean.class);
        log.info("{}", ret);
        log.info("{}", GsonUtils.toJsonPretty(ret));
    }

    @Test
    void test_004() {
        try {
            String jsonStr = "{\n" +
                "  \"name\" : \"name\",\n" +
                "  \"zonedDateTime\" : \"2024-11-02T12:35+08:00\",\n" +
                "  \"localDateTime\" : \"2024-11-02T12:35\",\n" +
                "  \"date\" : \"2024-11-02T12:35:22\"\n" +
                "}";
            final DateTimeBean ret = GsonUtils.fromJson(jsonStr, DateTimeBean.class);
            log.info("{}", ret);
            log.info("{}", GsonUtils.toJsonPretty(ret));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    @Test
    void test_005() {
        log.info("{}", GsonUtils.toJson("abc"));
    }
}
