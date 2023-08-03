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
    private boolean enable;
    private String tpMoCron;
    private String cpMoCron;
    private String commonErrorCron;
    private boolean scheduler;
}
