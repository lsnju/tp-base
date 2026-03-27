package com.lsnju.base.http5.config;

import org.apache.hc.client5.http.fluent.Request;

/**
 *
 * @author lisong
 * @since 2022/1/28 9:04
 * @version V1.0
 */
@FunctionalInterface
public interface Http5RequestCustomizer {

    Http5RequestCustomizer NO_OP = t -> {
    };

    void customize(Request request);

}
