package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2021/12/2 9:06
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "logger")
public class TpLogConfigProperties extends BaseMo {
    private String basePackage;
    private String sysName;
    private String level;
    private String home;
    private String path;
    private String hisPath;
    private String output;
    private String consolePattern;
    private String configExt;
    private String maxHistory;
}
