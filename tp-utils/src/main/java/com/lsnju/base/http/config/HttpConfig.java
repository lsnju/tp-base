package com.lsnju.base.http.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author lisong
 * @since 2021/11/5 11:48
 * @version V1.0
 */
@Getter
@Builder
@ToString
public class HttpConfig {
    private final String userAgent;
    private final int socketTimeout;
    private final int connectTimeout;
}
