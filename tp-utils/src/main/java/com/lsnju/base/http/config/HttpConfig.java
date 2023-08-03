package com.lsnju.base.http.config;

import com.lsnju.base.model.BaseMo;

import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2021/11/5 11:48
 * @version V1.0
 */
@Getter
@Builder
public class HttpConfig extends BaseMo {
    private final String userAgent;
    private final int socketTimeout;
    private final int connectTimeout;
}
