package com.lsnju.base.http.config;

import org.apache.http.client.fluent.Request;

/**
 *
 * @author lisong
 * @since 2022/1/28 9:04
 * @version V1.0
 */
@FunctionalInterface
public interface RequestCustomizer {

    RequestCustomizer NO_OP = t -> {
    };

    void customize(Request request);

}
