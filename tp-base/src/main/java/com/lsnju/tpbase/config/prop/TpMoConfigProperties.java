package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2022/8/11 10:42
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tp.sys.mo")
public class TpMoConfigProperties extends BaseMo {
    private String basePath = "/tp/mo";
    private String pagePath = "/tp";
    private String jdbcUrl = "/tp/test";
}
