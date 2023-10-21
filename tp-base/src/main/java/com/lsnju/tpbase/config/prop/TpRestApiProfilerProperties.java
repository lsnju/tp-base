package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2022/8/11 11:01
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rest.api.profiler")
public class TpRestApiProfilerProperties extends BaseMo {
    private long timeout = 1000;
    private String prefix;
}
