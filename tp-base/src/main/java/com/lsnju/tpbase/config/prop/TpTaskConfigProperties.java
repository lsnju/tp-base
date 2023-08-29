package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2022/1/22 15:49
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tp.task")
public class TpTaskConfigProperties {
    private boolean enable = true;
    private String tpMoCron = "*/5 * * * * ?";
    private String cpMoCron = "*/5 * * * * ?";
    private String commonErrorCron = "5 1 0 * * ?";
    private boolean tpMoEnable = true;
    private boolean cpMoEnable = true;
    private boolean commonErrorEnable = false;
//    private boolean scheduler;
}
